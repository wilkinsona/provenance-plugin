package org.springframework.provenance.gradle;

import java.io.File;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Provider;

public class ProvenancePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getTasks().register("provenance", GenerateProvenance.class, (task) -> {
			task.getProjectName().convention(project.provider(project::getName));
			task.getProjectVersion().convention(project.provider(() -> project.getVersion().toString()));
			task.getGitDirectory().convention(gitDirectory(project));
			task.getOutputDirectory().convention(project.getLayout().getBuildDirectory().dir("provenance"));
		});
	}
	
	private Provider<Directory> gitDirectory(Project project) {
		return project.getLayout().dir(project.provider(() -> new File(project.getLayout().getProjectDirectory().getAsFile().getCanonicalFile(), ".git")));
	}

}
