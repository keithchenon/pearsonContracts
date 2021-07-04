import javaposse.jobdsl.dsl.DslFactory

String gitlabSecretToken = "{AQAAABAAAAAQz0sLHum+XN5aTOj2ZYVGRpmozXNnR/roYgPTQEJ2WqA=}"

Closure masterMavenJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}") {
		triggers {
			gitlabPush {
				buildOnPushEvents()
				includeBranches("master")
			}
		}
		jdk "jdk"
		scm {
			git {
				remote {
					url "ssh://git@gitlab.example.com:9922/${group}/${name}.git"
					delegate.name("origin")
				}
				branch 'refs/remotes/origin/master'
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./mvnw clean deploy
					""")
		}
		publishers {
			archiveJunit "**/surefire-reports/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure prMavenJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}_pr") {
		triggers {
			gitlabPush {
				buildOnMergeRequestEvents()
			}
		}
		jdk "jdk"
		scm {
			git {
				remote {
					url '${gitlabSourceRepoURL}'
					delegate.name('${gitlabSourceRepoName}')
					refspec("+refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*")
				}
				branch '${gitlabSourceRepoName}/${gitlabSourceBranch}'
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./mvnw clean test
					""")
		}
		publishers {
			archiveJunit "**/surefire-reports/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure masterMavenCompatibilityJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}_compatibility") {
		parameters {
			stringParam("LATEST_PRODUCTION_VERSION", "0.0.1-SNAPSHOT", 'Latest version to run API compatibility against')
		}
		jdk "jdk"
		scm {
			git {
				remote {
					url "ssh://git@gitlab.example.com:9922/${group}/${name}.git"
				}
				branch "master"
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./mvnw clean test -Dlatest.production.version=\$LATEST_PRODUCTION_VERSION -Papicompatibility
					""")
		}
		publishers {
			archiveJunit "**/surefire-reports/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure masterGradleJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}") {
		triggers {
			gitlabPush {
				buildOnPushEvents()
			}
		}
		jdk "jdk"
		scm {
			git {
				remote {
					url "ssh://git@gitlab.example.com:9922/${group}/${name}.git"
					delegate.name("origin")
				}
				branch 'refs/remotes/origin/master'
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./gradlew clean build publish
					""")
		}
		publishers {
			archiveJunit "**/test-results/**/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure prGradleJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}_pr") {
		jdk "jdk"
		scm {
			git {
				remote {
					url '${gitlabSourceRepoURL}'
					delegate.name('${gitlabSourceRepoName}')
					refspec("+refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*")
				}
				branch '${gitlabSourceRepoName}/${gitlabSourceBranch}'
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./gradlew clean build
					""")
		}
		publishers {
			archiveJunit "**/test-results/**/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure masterGradleCompatibilityJob = { DslFactory dsl, String group, String name ->
	dsl.job("${group}_${name}_compatibility") {
		triggers {
			gitlabPush {
				buildOnPushEvents()
			}
		}
		parameters {
			stringParam("LATEST_PRODUCTION_VERSION", "0.0.1-SNAPSHOT", 'Latest version to run API compatibility against')
		}
		jdk "jdk"
		scm {
			git {
				remote {
					url "ssh://git@gitlab.example.com:9922/${group}/${name}.git"
				}
				branch "master"
			}
		}
		wrappers {
			timestamps()
			colorizeOutput()
			maskPasswords()
			timeout {
				noActivity(300)
				failBuild()
				writeDescription('Build failed due to timeout after {0} minutes of inactivity')
			}
		}
		steps {
			shell("""#!/bin/bash -x
					set -o errexit
					./gradlew clean build -PlatestProductionVersion=\$LATEST_PRODUCTION_VERSION -Papicompatibility
					""")
		}
		publishers {
			archiveJunit "**/test-results/**/*.xml"
		}
		configure {
			def publishers = (it / "publishers")
			def gitlabPublisher = (publishers / "com.dabsquared.gitlabjenkins.publisher.GitLabCommitStatusPublisher")
			(gitlabPublisher / "name").setValue("jenkins")
			(gitlabPublisher / "markUnstableAsSuccess").setValue(false)
			def property = (it / "properties" / "com.coravy.hudson.plugins.github.GithubProjectProperty")
			(property / "projectUrl").setValue("https://gitlab.example.com:9080/${group}/${name}/")
			(it / "triggers" / "com.dabsquared.gitlabjenkins.GitLabPushTrigger" / "secretToken").setValue(gitlabSecretToken)
		}
	}
}

Closure defaultColumns() {
	return {
		status()
		name()
		lastSuccess()
		lastFailure()
		lastBuildConsole()
		buildButton()
	}
}

Closure views = { DslFactory dsl ->
	dsl.listView('Seeds') {
		jobs {
			regex('.*-seed')
		}
		columns defaultColumns()
	}
	dsl.listView('07-01-provider-maven') {
		jobs {
			regex('07-01-provider-maven.*')
		}
		columns defaultColumns()
	}
	dsl.listView('07-02-provider-gradle') {
		jobs {
			regex('07-02-provider-gradle.*')
		}
		columns defaultColumns()
	}
	dsl.listView('07-03-cdc-producer') {
		jobs {
			regex('07-03-cdc-producer.*')
		}
		columns defaultColumns()
	}
	dsl.listView('07-04-cdc-external') {
		jobs {
			regex('07-04-cdc-external.*')
		}
		columns defaultColumns()
	}
	dsl.listView('07-05-cdc-git') {
		jobs {
			regex('07-05-cdc-git.*')
		}
		columns defaultColumns()
	}
}

masterMavenJob((DslFactory) this, "07-01-provider-maven", "producer")
masterMavenCompatibilityJob((DslFactory) this, "07-01-provider-maven", "producer")
masterMavenJob((DslFactory) this, "07-01-provider-maven", "consumer")
masterGradleJob((DslFactory) this, "07-02-provider-gradle", "producer")
masterGradleCompatibilityJob((DslFactory) this, "07-02-provider-gradle", "producer")
masterGradleJob((DslFactory) this, "07-02-provider-gradle", "consumer")
masterMavenJob((DslFactory) this, "07-03-cdc-producer", "producer")
prMavenJob((DslFactory) this, "07-03-cdc-producer", "producer")
masterMavenCompatibilityJob((DslFactory) this, "07-03-cdc-producer", "producer")
masterMavenJob((DslFactory) this, "07-03-cdc-producer", "consumer")
masterMavenJob((DslFactory) this, "07-04-cdc-external", "external")
masterMavenJob((DslFactory) this, "07-04-cdc-external", "producer")
prMavenJob((DslFactory) this, "07-04-cdc-external", "producer")
masterMavenCompatibilityJob((DslFactory) this, "07-04-cdc-external", "producer")
masterMavenJob((DslFactory) this, "07-04-cdc-external", "consumer")
masterMavenJob((DslFactory) this, "07-05-cdc-git", "producer")
prMavenJob((DslFactory) this, "07-05-cdc-git", "producer")
masterMavenCompatibilityJob((DslFactory) this, "07-05-cdc-git", "producer")
masterMavenJob((DslFactory) this, "07-05-cdc-git", "consumer")
views((DslFactory) this)