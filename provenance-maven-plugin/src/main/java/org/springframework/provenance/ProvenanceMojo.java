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
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import org.springframework.provenance.github.UrlParser;
import org.springframework.provenance.model.Project;
import org.springframework.provenance.model.Repository.Content;

/**
 * @author Michael Minella
 */
@Mojo(name = "provenance", defaultPhase = LifecyclePhase.PACKAGE,
		requiresProject = true, threadSafe = true,
		requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
		requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ProvenanceMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject mavenProject;

	@Parameter(defaultValue = "${project.name}", property = "projectName", required = false)
	private String projectName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		if(this.mavenProject.isExecutionRoot()) {
			try {
				Repository currentRepo = new FileRepositoryBuilder()
						.setGitDir(new File("./.git"))
						.build();

				String remoteUrl = currentRepo.getConfig()
						.getString("remote", "origin", "url");

				getLog().info("Git Origin URL: " + remoteUrl);
				getLog().info("Output directory: " + this.mavenProject.getBuild().getDirectory());

				URL url = UrlParser.parse(remoteUrl);

				Project project = new Project(this.projectName,
						this.mavenProject.getVersion());

				org.springframework.provenance.model.Repository repository =
						new org.springframework.provenance.model.Repository(Content.SOURCE,
								url.getHost(),
								"Git",
								url.getPath());

				project.addSourceRepository(repository);

				ObjectMapper mapper = new ObjectMapper();
				mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

				mapper.writerWithDefaultPrettyPrinter()
						.writeValue(
						Paths.get(
								this.mavenProject.getBuild()
										.getDirectory() + "/provenance.json").toFile(),
						project);
			}
			catch (IOException e) {
				throw new MojoFailureException("Unable to open git repository for this project", e);
			}
		}
	}
}
