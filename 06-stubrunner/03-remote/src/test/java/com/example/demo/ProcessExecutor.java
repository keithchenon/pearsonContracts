package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProcessExecutor {
	private static final Logger log = LoggerFactory.getLogger(ProcessExecutor.class);

	private String workingDir;

	ProcessExecutor(String workingDir) {
		this.workingDir = workingDir;
	}

	void runCommand(String command) {
		try {
			long waitTimeInMinutes = 2;
			String[] commands = new String[] { command };
			String workingDir = this.workingDir;
			log.debug("Will run the command from [{}] via {} and wait for result for [{}] minutes",
					workingDir, commands, waitTimeInMinutes);
			ProcessBuilder builder = builder(commands, workingDir);
			Process process = startProcess(builder);
			boolean finished = process.waitFor(waitTimeInMinutes, TimeUnit.MINUTES);
			if (!finished) {
				log.error("The command hasn't managed to finish in [{}] minutes", waitTimeInMinutes);
				process.destroyForcibly();
				throw new IllegalStateException("Process waiting time of [" + waitTimeInMinutes + "] minutes exceeded");
			}
			if (process.exitValue() != 0) {
				throw new IllegalStateException("The process has exited with exit code [" + process.exitValue() + "]");
			}
		}
		catch (InterruptedException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private Process startProcess(ProcessBuilder builder) throws IOException {
		return builder.start();
	}

	private ProcessBuilder builder(String[] commands, String workingDir) {
		return new ProcessBuilder(commands)
				.directory(new File(workingDir))
				.inheritIO();
	}
}