name := "monocle-example"

scalaVersion := "2.11.5"

version := "0.0.1"

val libraryVersion = "1.0.1"

libraryDependencies++=Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"    % libraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-generic" % libraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % libraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-law"     % libraryVersion % "test"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)
