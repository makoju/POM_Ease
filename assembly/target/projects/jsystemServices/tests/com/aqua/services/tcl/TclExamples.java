/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package com.aqua.services.tcl;

import java.io.File;
import java.io.FileInputStream;

import jsystem.extensions.analyzers.text.FindText;
import jsystem.sysobj.scripting.tcl.ShellCommand;
import jsystem.sysobj.scripting.tcl.TclShell;
import jsystem.sysobj.scripting.tcl.TclShellLocal;
import junit.framework.SystemTestCase;

public class TclExamples extends SystemTestCase {

	TclShell shell;
	
	public void setUp() throws Exception {
		shell = new TclShellLocal(new File("c:/Program Files/Tcl/bin/tclsh85.exe"));
		shell.launch();
	}
	
	public void testActivateScript() throws Exception {
		shell.source(new FileInputStream("C:/devspace/newewew/jsystemServices/tests/com/aqua/services/tcl/exampletclscript.tcl"));

		FindText textFinder = new FindText("hello tcl");
		textFinder.setTestAgainst(shell.getResults());
		textFinder.analyze();
		assertTrue(textFinder.getStatus());
		
		ShellCommand command = new ShellCommand("PrintText",new String[]{"Hello TCL procedure"});
		shell.executeCommand(command);
		textFinder = new FindText("Hello TCL procedure");
		textFinder.setTestAgainst(shell.getResults());
		textFinder.analyze();
		assertTrue(textFinder.getStatus());
		
		assertEquals("", command.getReturnValue());
		assertEquals("NONE", command.getErrorCode());
		assertEquals(null,command.getErrorString());
		
	}
	
	public void tearDown(){
		shell.exit();
	}
	
}
