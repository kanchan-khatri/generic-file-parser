package com.eb.renderview

import java.util.Properties

/*
 * Object ConfigManager: processes the application specific properties from resources/application.properties file
 * ConfigManager(<Key>) -> returns the <Value> of the property <Key>
 *                      -> returns empty string if Key does not exist
 */
object ConfigManager {

  val properties :Properties = loadConfig(  "application.properties")

  private def loadConfig(configFile: String): Properties= {
    try {
      val properties: Properties = new Properties()
      properties.load(getClass.getClassLoader.getResourceAsStream( "application.properties"))
      properties
    } catch  {
      case e:Exception =>
        println("Exception: Unable to read the application properties")
        throw e
    }
  }

  def apply(key:String) : String = {
    var p = properties.getProperty(key)
    if(p != null) p
    else ""
  }
}
