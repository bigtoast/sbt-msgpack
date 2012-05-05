MessagePack plugin for sbt 0.11+
====================================

# Instructions for use:
### Step 1: Include the plugin in your build

Add the following to your `project/plugins/build.sbt`:

## sbt-0.11.0

    resolvers += "bigtoast-github" at "http://bigtoast.github.com/repo/"

    addSbtPlugin("com.github.bigtoast" % "sbt-msgpack" % "0.1")

### Step 2: Add sbt-msgpack settings to your build

Add the following to your 'build.sbt' ( if you are using build.sbt )


    import com.github.bigtoast.sbtmsgpack.MsgPackPlugin

    seq(MsgPackPlugin.msgPackSettings: _*)

Or if you are using a build object extending from Build:

    import sbt._
    import Keys._
    import com.github.bigtoast.sbtmsgpack.MsgPackPlugin

    class BuildWithMsgPackShiz extends Build {
         lazy val seniorProject = Project("hola", file("."), settings = 
          Defaults.defaultSettings ++ MsgPackPlugin.msgPackSettings ++ Seq(/* custom settings go here */))
    }


## Settings

<table>
        <tr>
                <td> <b>msgpack-idl</b> </td>
                <td>MessagePack idl generator executable. This defaults to just 'msgpack-idl'</td>
        </tr>
        <tr><td></td><td>

            msgpackIdl := "/some/other/path/to/msgpack-idl"

        </td></tr>
        <tr>
                <td> <b>msgpackSourceDir</b> </td>
                <td>Directory containing thrift sources. This defaults to 'src/main/msgpack'.</td>
        </tr>
        <tr><td></td><td>

            msgpackSourceDir &lt;&lt;= baseDirectory( _ / "other" / "msgpack" / "sourceDir" )

        </td></tr>
        <tr>
                <td> <b>msgpackOutputDir</b> </td>
                <td>The output dir for the generated sources. This directory will be added to sourceManaged so it will be automatically get compiled when you run compile. This defaults to 'target/generated-sources'.</td>
        </tr>
        <tr>
                <td> <b>msgpackJavaOptions</b> </td>
                <td>Additional options to msgpack compiler for java generation.</td>
        </tr>
        <tr>
                <td> <b>msgpackJavaEnabled</b> </td>
                <td> Are we generating java source (?)  Default is true.</td>
        </tr>
        <tr>
                <td> <b>msgpackVersion</b> </td>
                <td> Version of the MessagePack lib. Default is 0.6.5.</td>
        </tr>
</table>

## Tasks

<table>
        <tr>
                <td> <b>msgpack:generate-java</b> </td>
                <td>This will run generate java sources from the msgpack sources. This task is automatically executed when compile is run.</td>
        </tr>
        
</table>


Warnings and Notes
------------------

 ## Currently the msgpack-idl doesn't generate current java code so this plugin isn't of much use. I didn't find this out until after writing the plugin of course.

If any bugs are found or features wanted please file an issue in the github project. I will do my best to accommodate.


Contributors
------------
Andrew Headrick [bigtoast]("http://github.com/bigtoast")

