 [SPARK-23036] Add withGlobalTempView for testing and correct some improper with view related method usage
    
    Add withGlobalTempView when create global temp view, like withTempView and withView.
    And correct some improper usage.



Add withGlobalTempView for testing and correct some improper with view related method usage

Add withGlobalTempView when create global temp view, like withTempView and withView.

And correct some improper usage like: 
{code:java}
 test("list global temp views") {
    try {
      sql("CREATE GLOBAL TEMP VIEW v1 AS SELECT 3, 4")
      sql("CREATE TEMP VIEW v2 AS SELECT 1, 2")

      checkAnswer(sql(s"SHOW TABLES IN $globalTempDB"),
        Row(globalTempDB, "v1", true) ::
        Row("", "v2", true) :: Nil)

      assert(spark.catalog.listTables(globalTempDB).collect().toSeq.map(_.name) == Seq("v1", "v2"))
    } finally {
      spark.catalog.dropTempView("v1")
      spark.catalog.dropGlobalTempView("v2")
    }
  }
{code}


[SPARK-23059][SQL][TEST] Correct some improper with view related method usage