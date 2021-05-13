/*
 * Copyright 2021-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.provenance;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.provenance.github.UrlParser;
import org.springframework.provenance.model.Project;
import org.springframework.provenance.model.Repository.Content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class Provenance {
	
	private final String name;
	
	private final String version;
	
	private final Logger logger;
	
	public Provenance(String name, String version, Logger logger) {
		this.name = name;
		this.version = version;
		this.logger = logger;
	}
	
	public void write(File outputDir) throws IOException {
		Path output = Files.createTempFile("origin-url", ".tmp");
		Process process = new ProcessBuilder("git", "remote", "get-url", "origin").redirectOutput(output.toFile()).start();
		try {
			int result = process.waitFor();
			if (result != 0) {
				throw new RuntimeException("git failed with exit value " + process.exitValue());
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted while waiting for git process");
		}
		List<String> lines = Files.readAllLines(output);
		if (lines.size() != 1) {
			throw new RuntimeException("Unexpected output from git");
		}	
		String remoteUrl = lines.get(0);

		logger.info("Git Origin URL: " + remoteUrl);
		logger.info("Output directory: " + outputDir);

		URL url = UrlParser.parse(remoteUrl);

		Project project = new Project(name, version);

		org.springframework.provenance.model.Repository repository =
				new org.springframework.provenance.model.Repository(Content.SOURCE,
						url.getHost(),
						"Git",
						url.getPath());

		project.addSourceRepository(repository);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		
		outputDir.mkdirs();

		mapper.writerWithDefaultPrettyPrinter()
				.writeValue(
				Paths.get(outputDir + "/provenance.json").toFile(),
				project);
	}

}
