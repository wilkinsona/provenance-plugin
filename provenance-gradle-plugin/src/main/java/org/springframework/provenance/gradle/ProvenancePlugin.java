package org.springframework.provenance.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ProvenancePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getTasks().register("provenance", GenerateProvenance.class, (task) -> {
			task.getProjectName().convention(project.provider(project::getName));
			task.getProjectVersion().convention(project.provider(() -> project.getVersion().toString()));
			task.getOutputDirectory().convention(project.getLayout().getBuildDirectory().dir("provenance"));
		});
	}

}
