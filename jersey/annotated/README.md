does not work reliable:
* sometimes fails during startup because it cannot read the applicationContext.xml, which is not wanted here in the first place
* @ApplicationPath WITH path-string is needed, in order for the jersey application to work (at least "/")
* only spring 3 up to now ??