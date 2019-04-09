
CREATE table parquet_table
USING org.apache.spark.sql.parquet
OPTIONS (
    path 'file:/tmp/spark-4416b5f7-015a-4d2e-810d-5e1c928cee6b/'


)



      CREATE TEMPORARY VIEW partitioned_parquet_with_key
      USING org.apache.spark.sql.parquet
      OPTIONS (
        path 'file:/tmp/spark-5ca72496-ba66-4b1f-9183-aeba58da8e53/'
      )
    
        