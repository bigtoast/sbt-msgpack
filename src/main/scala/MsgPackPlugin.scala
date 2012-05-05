
package com.github.bigtoast.sbtmsgpack

import sbt._
import classpath._
import Process._
import Keys._

object MsgPackPlugin extends Plugin {

  val msgpackConfig = config("msgpack")

  val msgpackIdl         = SettingKey[String]("msgpack-idl", "msgpack-idl executable")
  val msgpackSourceDir   = SettingKey[File]("source-directory", "Source directory for thrift files. Defaults to src/main/msgpack")
  val msgpackGenerate    = TaskKey[Seq[File]]("generate-java", "Generate java sources from msgpack files")
  val msgpackOutputDir   = SettingKey[File]("output-directory", "Directory where the java files should be placed. Defaults to sourceManaged")
  val msgpackJavaOptions = SettingKey[Seq[String]]("msgpack-java-options", "additional options for java msgpack generation")
  val msgpackJavaEnabled = SettingKey[Boolean]("java-enabled", "java generation is enabled. Default - yes")
  val msgpackVersion     = SettingKey[String]("msgpack-version", "The version of MessagePack used.")
  
  lazy val msgpackSettings :Seq[Setting[_]] = inConfig(msgpackConfig)(Seq[Setting[_]](
       msgpackIdl := "msgpack-idl",

       msgpackSourceDir <<= (sourceDirectory in Compile){ _ / "msgpack"},

       msgpackOutputDir <<= (sourceManaged in Compile){ _ / "msgpack" }.identity,

       msgpackJavaEnabled := true,
       
       msgpackVersion := "0.6.5",

       msgpackJavaOptions := Seq[String](),

       msgpackGenerate <<= (streams, msgpackSourceDir, msgpackOutputDir, msgpackIdl, msgpackJavaOptions, msgpackJavaEnabled) map { 
         (out, sdir, odir, tbin, opts, enabled ) =>
           if (enabled) {
             compileMsgpack(sdir, odir, tbin, "java", opts, out.log)
          } else {
             Seq[File]()
         }
       },

      managedClasspath <<= (classpathTypes, update) map { (cpt, up) =>  
        Classpaths.managedJars(msgpackConfig, cpt, up)
      }

  )) ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= msgpackGenerate in msgpackConfig,
    ivyConfigurations += msgpackConfig,
    libraryDependencies <+= ( msgpackVersion in msgpackConfig )( "org.msgpack" % "msgpack" % _ )
  )

  def compileMsgpack(sourceDir: File, 
                    outputDir: File, 
                    msgpackBin: String, 
                    language: String, 
                    options: Seq[String],
                    logger: Logger):Seq[File] =
  {
    val schemas = (sourceDir ** "*.msgpack").get
    outputDir.mkdirs()
    logger.info("Compiling %d msgpack files to %s in %s".format(schemas.size, language, outputDir))
    schemas.foreach { schema =>
      val cmd = "%s -g %s %s -o %s".format(msgpackBin, 
                                        language,
                                        schema, outputDir )
      logger.info("Compiling schema with command: %s" format cmd)
      <x>{cmd}</x> !
    }
    (outputDir ** "*.%s".format(language)).get.toSeq
  }


}
