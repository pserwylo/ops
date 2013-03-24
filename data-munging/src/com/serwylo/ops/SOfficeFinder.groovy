package com.serwylo.ops

/**
 * TODO: Will be moved to groovy-uno project eventually.
 * Hats off to http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 */
abstract class SOfficeFinder {

	protected File searchDir

	public static SOfficeFinder createFinder() {
		List<SOfficeFinder> finders = [
			new UnixSOfficeFinder(),
			new WindowsSOfficeFinder(),
			new MacSOfficeFinder(),
			new GenericSOfficeFinder()
		]

		finders.find { it.isForRunningOs() }
	}

	protected String getOs() {
		System.getProperty( "os.name" ).toLowerCase()
	}

	abstract protected String isForRunningOs()

}

class UnixSOfficeFinder extends SOfficeFinder {

	@Override
	protected String isForRunningOs() {
		String os = getOs()
		os.indexOf( "nix" ) >= 0 || os.indexOf( "nux" ) >= 0 || os.indexOf( "aix" ) > 0
	}
}

class WindowsSOfficeFinder extends SOfficeFinder {

	@Override
	protected String isForRunningOs() {
		os.contains( "win" )
	}
}

class MacSOfficeFinder extends SOfficeFinder {

	@Override
	protected String isForRunningOs() {
		os.contains( "mac" )
	}
}

class GenericSOfficeFinder extends SOfficeFinder {

	@Override
	protected String isForRunningOs() {
		true
	}
}