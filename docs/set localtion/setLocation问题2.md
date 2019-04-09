 [SPARK-23039][SQL] Fix the bug in alter table set location.
1c073856b0c619fe533dd94cbb19193a660c1158ESC


TOBO work: Fix the bug in alter table set location.   
org.apache.spark.sql.execution.command.DDLSuite#testSetLocation

{code:java}
    // TODO(gatorsmile): fix the bug in alter table set location.
   //    if (isUsingHiveMetastore) {
    //    assert(storageFormat.properties.get("path") === expected)
    //   }
{code}

Analysis:

because user add locationUri and erase path by 
{code:java}
 newPath = None
{code}
in org.apache.spark.sql.hive.HiveExternalCatalog#restoreDataSourceTable:

{code:java}
val storageWithLocation = {
      val tableLocation = getLocationFromStorageProps(table)
      // We pass None as `newPath` here, to remove the path option in storage properties.
      updateLocationInStorageProps(table, newPath = None).copy(
        locationUri = tableLocation.map(CatalogUtils.stringToURI(_)))
    }
{code}

because " We pass None as `newPath` here, to remove the path option in storage properties."

And locationUri is obtain from path in storage properties

{code:java}
  private def getLocationFromStorageProps(table: CatalogTable): Option[String] = {
    CaseInsensitiveMap(table.storage.properties).get("path")
  }
{code}

So we can use locationUri instead path
