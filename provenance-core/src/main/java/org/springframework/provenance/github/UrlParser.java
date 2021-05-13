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

package org.springframework.provenance.github;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A java implementation of the URL parser built in Node found here:
 * https://github.com/repo-utils/parse-github-repo-url/blob/master/index.js
 *
 * @author Michael Minella
 */
public class UrlParser {

	public static URL parse(String url) throws MalformedURLException {
//		// user/repo#version
//		var m = /^([\w-.]+)\/([\w-.]+)((?:#|@).+)?$/.exec(string)
//		if (m) return format(m)
//
		System.out.println(">> url = " + url);
		String normalizedUrl = normalize(url);
		System.out.println(">> normalizedURL = " + normalizedUrl);

		if(!normalizedUrl.contains("://")) {
			throw new MalformedURLException("Invalid URL: " + url);
		}

		URL parsedUrl = new URL(normalizedUrl);

		System.out.println(">> Host: " + parsedUrl.getHost());
		System.out.println(">> Path: " + parsedUrl.getPath());
		System.out.println(">> Protocol: " + parsedUrl.getProtocol());

		return parsedUrl;

//		var path = url.pathname.replace(/\.git$/, '')
//
//		// https://www.npmjs.org/doc/json.html#Git-URLs-as-Dependencies
//		var m = /^\/([\w-.]+)\/([\w-.]+)$/.exec(path)
//		if (m) return m.slice(1, 3).concat((url.hash || '').slice(1))
//
//		// archive link
//		// https://developer.github.com/v3/repos/contents/#get-archive-link
//		var m = /^\/repos\/([\w-.]+)\/([\w-.]+)\/(?:tarball|zipball)(\/.+)?$/.exec(path)
//		if (m) return format(m)
//
//		// codeload link
//		// https://developer.github.com/v3/repos/contents/#response-4
//		var m = /^\/([\w-.]+)\/([\w-.]+)\/(?:legacy\.(?:zip|tar\.gz))(\/.+)?$/.exec(path)
//		if (m) return format(m)
//
//		// tarball link
//		// https://github.com/LearnBoost/socket.io-client/blob/master/package.json#L14
//		var m = /^\/([\w-]+)\/([\w-.]+)\/archive\/(.+)\.tar\.gz?$/.exec(path)
//		if (m) return m.slice(1, 4)
//
//		// https://docs.gitlab.com/ce/user/group/subgroups/
//		if (~url.host.indexOf('gitlab')) {
//			var m = /^\/((?:[\w-.]+\/)+)([\w-.]+)$/.exec(path)
//			if (m) {
//				m = m.slice(1, 3);
//				// remove slash at the end
//				m[0] = m[0].slice(0, -1);
//				return m.concat((url.hash || '').slice(1));
//			}
//		}
//
//		return false
//	}
//
//	function format(m) {
//		var version = (m[3] || '').slice(1)
//		if (/^['"]/.test(version)) version = version.slice(1, -1)
//		return [m[1], m[2], version]
//	}
	}

	private static String normalize(String url) {

		return url.replace("//www.", "//")
				.replace("git@", "https://")
				.replace("https:git@/", "https://")
				.replace(".com:", ".com/");
	}

}
