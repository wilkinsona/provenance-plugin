package org.springframework.provenance.gradle;

import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.springframework.provenance.Provenance;

public class GenerateProvenance extends DefaultTask {
	
	private final DirectoryProperty gitDirectory;
	
	private final Property<String> name;
	
	private final Property<String> version;
	
	private final DirectoryProperty outputDirectory;
	
	public GenerateProvenance() {
		ObjectFactory objects = getProject().getObjects();
		this.gitDirectory = objects.directoryProperty();
		this.outputDirectory = objects.directoryProperty();
		this.name = objects.property(String.class);
		this.version = objects.property(String.class);
	}
	
	@InputDirectory
	public DirectoryProperty getGitDirectory() {
		return gitDirectory;
	}
	
	@OutputDirectory
	public DirectoryProperty getOutputDirectory() {
		return this.outputDirectory;
	}

	@Input
	public Property<String> getProjectName() {
		return name;
	}

	@Input
	public Property<String> getProjectVersion() {
		return version;
	}
	
	@TaskAction
	public void generateProvenance() throws IOException {
		new Provenance(this.gitDirectory.get().getAsFile(), this.name.get(), version.get(), 
				(message) -> getLogger().info(message)).write(this.outputDirectory.get().getAsFile());
	}

}
