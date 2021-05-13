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

/**
 *         {
 *             "content": "source",
 *             "host": "perforce.eng.vmware.com:1800",
 *             "path": "//depot/vrops/v8.3.0/projects/mps/nsx-v-solution/",
 *             "ref": "6280",
 *             "protocol": "Perforce"
 *         }
 * @author Michael Minella
 */
public class Repository implements Comparable<Repository> {

	private Content content;
	private String host;
	private String protocol;
	private String path;

	public Repository(Content content, String host, String protocol, String path) {
		this.content = content;
		this.host = host;
		this.protocol = protocol;
		this.path = path;
	}

	public Content getContent() {
		return content;
	}

	public String getHost() {
		return host;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(Repository repository) {

		if(repository.equals(this)) {
			return 0;
		}
		else {
			String thisUrl = this.protocol + this.host + this.path;
			String repositoryUrl = repository.protocol + repository.host + repository.path;

			return thisUrl.compareTo(repositoryUrl);
		}
	}

	public enum Content {

		SOURCE("source"), BINARY("binary");

		String value;

		Content(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}
	}
}
