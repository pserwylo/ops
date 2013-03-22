package com.serwylo.ops.gui

import com.serwylo.ops.Phase

class ProgressEvent {

	double progress
	String currentDescription
	boolean indeterminate = false
	Phase  owner

}
