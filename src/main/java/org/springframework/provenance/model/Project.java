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

package org.springframework.provenance.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * {
 *     "name": "vROps MP for NSX-v",
 *     "version": "3.7",
 *     "build_number": "17336671",
 *     "source_repositories": [
 *         {
 *             "content": "source",
 *             "host": "perforce.eng.vmware.com:1800",
 *             "path": "//depot/vrops/v8.3.0/projects/mps/nsx-v-solution/",
 *             "ref": "6280",
 *             "protocol": "Perforce"
 *         }
 *     ],
 *     "artifact_repositories": [],
 *     "components": []
 * }
 *
 * @author Michael Minella
 */
public class Project {

	private String name;
	private String version;
	private String buildNumber;
	private Set<Repository> sourceRepositories = new TreeSet<>();
	private Set<Repository> artifactRepositories = new TreeSet<>();
	private Set<String> components = new TreeSet<>();

	public Project(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public void addSourceRepository(Repository repository) {
		this.sourceRepositories.add(repository);
	}

	public void addArtifactRepository(Repository repository) {
		this.artifactRepositories.add(repository);
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Set<Repository> getSourceRepositories() {
		return sourceRepositories;
	}

	public Set<Repository> getArtifactRepositories() {
		return artifactRepositories;
	}

	public Set<String> getComponents() {
		return components;
	}
}
