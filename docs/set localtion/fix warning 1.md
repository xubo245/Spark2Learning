Fix warning: TEMPORARY TABLE ... USING ... is deprecated and use TempViewAlreadyExistsException when create temp view

There are warning when run test: test("rename temporary view - destination table with database name")

Another problem, it throw TempTableAlreadyExistsException and output "Temporary table '$table' already exists" when we create temp view by using org.apache.spark.sql.catalyst.catalog.GlobalTempViewManager#create, it's improper.

Please see jira

## What changes were proposed in this pull request?

Fix some warning by changing "TEMPORARY TABLE ... USING ... " to "TEMPORARY VIEW ... USING 
... "

Fix improper information about TempTableAlreadyExistsException when create temp view

## How was this patch tested?

use old test cases, such as " test("create temporary view using") "


