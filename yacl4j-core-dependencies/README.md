# yacl4j-repackaged-jackson

This module repackages all Jackson-related dependencies.

### NOTE

According to [this](http://central.sonatype.org/pages/requirements.html#supply-javadoc-and-sources) OSS requirement:

> Projects with packaging other than *pom* have to supply JAR files that contain Javadoc and sources. ... If, for some reason (for example, license issue or it's a Scala project), you can not provide *-sources.jar* or *-javadoc.jar* , please make fake *-sources.jar* or *-javadoc.jar* with simple README inside to pass the checking.

We are generating the *-source.jar* through the maven-shade-plugin but, because this module does not directly contain any source, the *-javadoc.jar* is not automatically generated. So we are creating a fake *-javadoc.jar* which contains only this file.