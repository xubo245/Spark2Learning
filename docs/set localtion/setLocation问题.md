代码
		  test("SET LOCATION for managed table with partition") {
	    withTable("tbl_partition") {
	      withTempDir { dir =>
	
	        sql("CREATE TABLE tbl_partition(col1 INT, col2 INT) USING parquet PARTITIONED BY (col1)")
	        sql("INSERT INTO tbl_partition PARTITION(col1=1) SELECT 11")
	        sql("INSERT INTO tbl_partition PARTITION(col1=2) SELECT 22")
	        checkAnswer(spark.table("tbl_partition"), Seq(Row(11, 1), Row(22, 2)))
	        val defaultTablePath = spark.sessionState.catalog
	          .getTableMetadata(TableIdentifier("tbl_partition")).storage.locationUri.get
	        try {
	          sql(s"ALTER TABLE tbl_partition PARTITION (col1='1') SET LOCATION '${dir.toURI}'")
	          sql(s"ALTER TABLE tbl_partition PARTITION (col1='2') SET LOCATION '${dir.toURI}'")
	          spark.catalog.refreshTable("tbl_partition")
	          // SET LOCATION won't move data from previous table path to new table path.
	          assert(spark.table("tbl_partition").count() == 0)
	          // the previous table path should be still there.
	          assert(new File(defaultTablePath).exists())
	          spark.table("tbl_partition").show()
	          
	//          sql("INSERT INTO tbl_partition PARTITION(col1=2) SELECT 33")
	//          checkAnswer(spark.table("tbl_partition"), Row(33, 2))
	          // newly inserted data will go to the new table path.
	          assert(dir.listFiles().nonEmpty)
	
	          sql("DROP TABLE tbl_partition")
	          // the new table path will be removed after DROP TABLE.
	          assert(!dir.exists())
	        } finally {
	          Utils.deleteRecursively(new File(defaultTablePath))
	        }
	      }
	    }
	  }

错误：
	
	/usr/java/jdk1.8.0_131/bin/java -javaagent:/david/idea-IC-171.4424.56/lib/idea_rt.jar=34220:/david/idea-IC-171.4424.56/bin -Dfile.encoding=UTF-8 -classpath /root/.IdeaIC2017.1/config/plugins/Scala/lib/scala-plugin-runners.jar:/usr/java/jdk1.8.0_131/jre/lib/charsets.jar:/usr/java/jdk1.8.0_131/jre/lib/deploy.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/cldrdata.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/dnsns.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/jaccess.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/jfxrt.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/localedata.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/nashorn.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/sunec.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/sunjce_provider.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/sunpkcs11.jar:/usr/java/jdk1.8.0_131/jre/lib/ext/zipfs.jar:/usr/java/jdk1.8.0_131/jre/lib/javaws.jar:/usr/java/jdk1.8.0_131/jre/lib/jce.jar:/usr/java/jdk1.8.0_131/jre/lib/jfr.jar:/usr/java/jdk1.8.0_131/jre/lib/jfxswt.jar:/usr/java/jdk1.8.0_131/jre/lib/jsse.jar:/usr/java/jdk1.8.0_131/jre/lib/management-agent.jar:/usr/java/jdk1.8.0_131/jre/lib/plugin.jar:/usr/java/jdk1.8.0_131/jre/lib/resources.jar:/usr/java/jdk1.8.0_131/jre/lib/rt.jar:/xubo/git/spark/sql/hive/target/scala-2.11/test-classes:/xubo/git/spark/sql/hive/target/scala-2.11/classes:/david/repo/com/twitter/parquet-hadoop-bundle/1.6.0/parquet-hadoop-bundle-1.6.0.jar:/xubo/git/spark/core/target/scala-2.11/classes:/david/repo/com/twitter/chill_2.11/0.8.4/chill_2.11-0.8.4.jar:/david/repo/com/esotericsoftware/kryo-shaded/3.0.3/kryo-shaded-3.0.3.jar:/david/repo/com/esotericsoftware/minlog/1.3.0/minlog-1.3.0.jar:/david/repo/org/objenesis/objenesis/2.1/objenesis-2.1.jar:/david/repo/com/twitter/chill-java/0.8.4/chill-java-0.8.4.jar:/david/repo/org/apache/xbean/xbean-asm5-shaded/4.4/xbean-asm5-shaded-4.4.jar:/david/repo/org/apache/hadoop/hadoop-client/2.6.5/hadoop-client-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-common/2.6.5/hadoop-common-2.6.5.jar:/david/repo/xmlenc/xmlenc/0.52/xmlenc-0.52.jar:/david/repo/commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar:/david/repo/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar:/david/repo/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:/david/repo/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:/david/repo/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar:/david/repo/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:/david/repo/org/apache/hadoop/hadoop-auth/2.6.5/hadoop-auth-2.6.5.jar:/david/repo/org/apache/directory/server/apacheds-kerberos-codec/2.0.0-M15/apacheds-kerberos-codec-2.0.0-M15.jar:/david/repo/org/apache/directory/server/apacheds-i18n/2.0.0-M15/apacheds-i18n-2.0.0-M15.jar:/david/repo/org/apache/directory/api/api-asn1-api/1.0.0-M20/api-asn1-api-1.0.0-M20.jar:/david/repo/org/apache/directory/api/api-util/1.0.0-M20/api-util-1.0.0-M20.jar:/david/repo/org/apache/curator/curator-client/2.6.0/curator-client-2.6.0.jar:/david/repo/org/htrace/htrace-core/3.0.4/htrace-core-3.0.4.jar:/david/repo/org/apache/hadoop/hadoop-hdfs/2.6.5/hadoop-hdfs-2.6.5.jar:/david/repo/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:/david/repo/xerces/xercesImpl/2.9.1/xercesImpl-2.9.1.jar:/david/repo/xml-apis/xml-apis/1.4.01/xml-apis-1.4.01.jar:/david/repo/org/apache/hadoop/hadoop-mapreduce-client-app/2.6.5/hadoop-mapreduce-client-app-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-mapreduce-client-common/2.6.5/hadoop-mapreduce-client-common-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-yarn-client/2.6.5/hadoop-yarn-client-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-yarn-server-common/2.6.5/hadoop-yarn-server-common-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-mapreduce-client-shuffle/2.6.5/hadoop-mapreduce-client-shuffle-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-yarn-api/2.6.5/hadoop-yarn-api-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-mapreduce-client-core/2.6.5/hadoop-mapreduce-client-core-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-yarn-common/2.6.5/hadoop-yarn-common-2.6.5.jar:/david/repo/javax/xml/bind/jaxb-api/2.2.2/jaxb-api-2.2.2.jar:/david/repo/javax/xml/stream/stax-api/1.0-2/stax-api-1.0-2.jar:/david/repo/org/codehaus/jackson/jackson-jaxrs/1.9.13/jackson-jaxrs-1.9.13.jar:/david/repo/org/codehaus/jackson/jackson-xc/1.9.13/jackson-xc-1.9.13.jar:/david/repo/org/apache/hadoop/hadoop-mapreduce-client-jobclient/2.6.5/hadoop-mapreduce-client-jobclient-2.6.5.jar:/david/repo/org/apache/hadoop/hadoop-annotations/2.6.5/hadoop-annotations-2.6.5.jar:/xubo/git/spark/launcher/target/scala-2.11/classes:/xubo/git/spark/common/kvstore/target/scala-2.11/classes:/david/repo/org/fusesource/leveldbjni/leveldbjni-all/1.8/leveldbjni-all-1.8.jar:/david/repo/com/fasterxml/jackson/core/jackson-core/2.6.7/jackson-core-2.6.7.jar:/david/repo/com/fasterxml/jackson/core/jackson-annotations/2.6.7/jackson-annotations-2.6.7.jar:/xubo/git/spark/common/network-common/target/scala-2.11/classes:/xubo/git/spark/common/network-shuffle/target/scala-2.11/classes:/xubo/git/spark/common/unsafe/target/scala-2.11/classes:/david/repo/net/java/dev/jets3t/jets3t/0.9.4/jets3t-0.9.4.jar:/david/repo/javax/activation/activation/1.1.1/activation-1.1.1.jar:/david/repo/org/bouncycastle/bcprov-jdk15on/1.58/bcprov-jdk15on-1.58.jar:/david/repo/com/jamesmurty/utils/java-xmlbuilder/1.1/java-xmlbuilder-1.1.jar:/david/repo/net/iharder/base64/2.3.8/base64-2.3.8.jar:/david/repo/org/apache/curator/curator-recipes/2.6.0/curator-recipes-2.6.0.jar:/david/repo/org/apache/curator/curator-framework/2.6.0/curator-framework-2.6.0.jar:/david/repo/org/apache/zookeeper/zookeeper/3.4.6/zookeeper-3.4.6.jar:/david/repo/org/eclipse/jetty/jetty-plus/9.3.20.v20170531/jetty-plus-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-webapp/9.3.20.v20170531/jetty-webapp-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-xml/9.3.20.v20170531/jetty-xml-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-jndi/9.3.20.v20170531/jetty-jndi-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-security/9.3.20.v20170531/jetty-security-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-util/9.3.20.v20170531/jetty-util-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-server/9.3.20.v20170531/jetty-server-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-io/9.3.20.v20170531/jetty-io-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-http/9.3.20.v20170531/jetty-http-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-continuation/9.3.20.v20170531/jetty-continuation-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-servlet/9.3.20.v20170531/jetty-servlet-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-proxy/9.3.20.v20170531/jetty-proxy-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-client/9.3.20.v20170531/jetty-client-9.3.20.v20170531.jar:/david/repo/org/eclipse/jetty/jetty-servlets/9.3.20.v20170531/jetty-servlets-9.3.20.v20170531.jar:/david/repo/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/david/repo/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:/david/repo/org/apache/commons/commons-math3/3.4.1/commons-math3-3.4.1.jar:/david/repo/org/slf4j/slf4j-api/1.7.16/slf4j-api-1.7.16.jar:/david/repo/org/slf4j/jul-to-slf4j/1.7.16/jul-to-slf4j-1.7.16.jar:/david/repo/org/slf4j/jcl-over-slf4j/1.7.16/jcl-over-slf4j-1.7.16.jar:/david/repo/log4j/log4j/1.2.17/log4j-1.2.17.jar:/david/repo/org/slf4j/slf4j-log4j12/1.7.16/slf4j-log4j12-1.7.16.jar:/david/repo/com/ning/compress-lzf/1.0.3/compress-lzf-1.0.3.jar:/david/repo/org/xerial/snappy/snappy-java/1.1.2.6/snappy-java-1.1.2.6.jar:/david/repo/org/lz4/lz4-java/1.4.0/lz4-java-1.4.0.jar:/david/repo/com/github/luben/zstd-jni/1.3.2-2/zstd-jni-1.3.2-2.jar:/david/repo/org/roaringbitmap/RoaringBitmap/0.5.11/RoaringBitmap-0.5.11.jar:/david/repo/commons-net/commons-net/2.2/commons-net-2.2.jar:/david/repo/org/scala-lang/scala-library/2.11.8/scala-library-2.11.8.jar:/david/repo/org/json4s/json4s-jackson_2.11/3.2.11/json4s-jackson_2.11-3.2.11.jar:/david/repo/org/json4s/json4s-core_2.11/3.2.11/json4s-core_2.11-3.2.11.jar:/david/repo/org/json4s/json4s-ast_2.11/3.2.11/json4s-ast_2.11-3.2.11.jar:/david/repo/org/scala-lang/scalap/2.11.8/scalap-2.11.8.jar:/david/repo/org/glassfish/jersey/core/jersey-client/2.22.2/jersey-client-2.22.2.jar:/david/repo/javax/ws/rs/javax.ws.rs-api/2.0.1/javax.ws.rs-api-2.0.1.jar:/david/repo/org/glassfish/hk2/hk2-api/2.4.0-b34/hk2-api-2.4.0-b34.jar:/david/repo/org/glassfish/hk2/hk2-utils/2.4.0-b34/hk2-utils-2.4.0-b34.jar:/david/repo/org/glassfish/hk2/external/aopalliance-repackaged/2.4.0-b34/aopalliance-repackaged-2.4.0-b34.jar:/david/repo/org/glassfish/hk2/external/javax.inject/2.4.0-b34/javax.inject-2.4.0-b34.jar:/david/repo/org/glassfish/hk2/hk2-locator/2.4.0-b34/hk2-locator-2.4.0-b34.jar:/david/repo/org/javassist/javassist/3.18.1-GA/javassist-3.18.1-GA.jar:/david/repo/org/glassfish/jersey/core/jersey-common/2.22.2/jersey-common-2.22.2.jar:/david/repo/javax/annotation/javax.annotation-api/1.2/javax.annotation-api-1.2.jar:/david/repo/org/glassfish/jersey/bundles/repackaged/jersey-guava/2.22.2/jersey-guava-2.22.2.jar:/david/repo/org/glassfish/hk2/osgi-resource-locator/1.0.1/osgi-resource-locator-1.0.1.jar:/david/repo/org/glassfish/jersey/core/jersey-server/2.22.2/jersey-server-2.22.2.jar:/david/repo/org/glassfish/jersey/media/jersey-media-jaxb/2.22.2/jersey-media-jaxb-2.22.2.jar:/david/repo/javax/validation/validation-api/1.1.0.Final/validation-api-1.1.0.Final.jar:/david/repo/org/glassfish/jersey/containers/jersey-container-servlet/2.22.2/jersey-container-servlet-2.22.2.jar:/david/repo/org/glassfish/jersey/containers/jersey-container-servlet-core/2.22.2/jersey-container-servlet-core-2.22.2.jar:/david/repo/io/netty/netty-all/4.1.17.Final/netty-all-4.1.17.Final.jar:/david/repo/io/netty/netty/3.9.9.Final/netty-3.9.9.Final.jar:/david/repo/com/clearspring/analytics/stream/2.7.0/stream-2.7.0.jar:/david/repo/io/dropwizard/metrics/metrics-core/3.1.5/metrics-core-3.1.5.jar:/david/repo/io/dropwizard/metrics/metrics-jvm/3.1.5/metrics-jvm-3.1.5.jar:/david/repo/io/dropwizard/metrics/metrics-json/3.1.5/metrics-json-3.1.5.jar:/david/repo/io/dropwizard/metrics/metrics-graphite/3.1.5/metrics-graphite-3.1.5.jar:/david/repo/com/fasterxml/jackson/core/jackson-databind/2.6.7.1/jackson-databind-2.6.7.1.jar:/david/repo/com/fasterxml/jackson/module/jackson-module-scala_2.11/2.6.7.1/jackson-module-scala_2.11-2.6.7.1.jar:/david/repo/com/fasterxml/jackson/module/jackson-module-paranamer/2.7.9/jackson-module-paranamer-2.7.9.jar:/david/repo/org/apache/ivy/ivy/2.4.0/ivy-2.4.0.jar:/david/repo/oro/oro/2.0.8/oro-2.0.8.jar:/david/repo/net/razorvine/pyrolite/4.13/pyrolite-4.13.jar:/david/repo/net/sf/py4j/py4j/0.10.6/py4j-0.10.6.jar:/xubo/git/spark/common/tags/target/scala-2.11/classes:/david/repo/org/apache/commons/commons-crypto/1.0.0/commons-crypto-1.0.0.jar:/xubo/git/spark/core/target/scala-2.11/test-classes:/xubo/git/spark/sql/core/target/scala-2.11/classes:/david/repo/com/univocity/univocity-parsers/2.5.9/univocity-parsers-2.5.9.jar:/xubo/git/spark/common/sketch/target/scala-2.11/classes:/xubo/git/spark/sql/catalyst/target/scala-2.11/classes:/david/repo/org/apache/orc/orc-core/1.4.1/orc-core-1.4.1-nohive.jar:/david/repo/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar:/david/repo/io/airlift/aircompressor/0.8/aircompressor-0.8.jar:/david/repo/org/apache/orc/orc-mapreduce/1.4.1/orc-mapreduce-1.4.1-nohive.jar:/david/repo/org/apache/parquet/parquet-column/1.8.2/parquet-column-1.8.2.jar:/david/repo/org/apache/parquet/parquet-common/1.8.2/parquet-common-1.8.2.jar:/david/repo/org/apache/parquet/parquet-encoding/1.8.2/parquet-encoding-1.8.2.jar:/david/repo/org/apache/parquet/parquet-hadoop/1.8.2/parquet-hadoop-1.8.2.jar:/david/repo/org/apache/parquet/parquet-format/2.3.1/parquet-format-2.3.1.jar:/david/repo/org/apache/parquet/parquet-jackson/1.8.2/parquet-jackson-1.8.2.jar:/david/repo/org/apache/arrow/arrow-vector/0.8.0/arrow-vector-0.8.0.jar:/david/repo/org/apache/arrow/arrow-format/0.8.0/arrow-format-0.8.0.jar:/david/repo/org/apache/arrow/arrow-memory/0.8.0/arrow-memory-0.8.0.jar:/david/repo/com/carrotsearch/hppc/0.7.2/hppc-0.7.2.jar:/david/repo/com/vlkan/flatbuffers/1.2.0-3f79e055/flatbuffers-1.2.0-3f79e055.jar:/xubo/git/spark/sql/core/target/scala-2.11/test-classes:/xubo/git/spark/sql/catalyst/target/scala-2.11/test-classes:/david/repo/org/scala-lang/scala-reflect/2.11.8/scala-reflect-2.11.8.jar:/david/repo/org/scala-lang/modules/scala-parser-combinators_2.11/1.0.4/scala-parser-combinators_2.11-1.0.4.jar:/david/repo/org/codehaus/janino/janino/3.0.8/janino-3.0.8.jar:/david/repo/org/codehaus/janino/commons-compiler/3.0.8/commons-compiler-3.0.8.jar:/david/repo/org/antlr/antlr4-runtime/4.7/antlr4-runtime-4.7.jar:/xubo/git/spark/common/tags/target/scala-2.11/test-classes:/david/repo/org/spark-project/hive/hive-exec/1.2.1.spark2/hive-exec-1.2.1.spark2.jar:/david/repo/commons-io/commons-io/2.4/commons-io-2.4.jar:/david/repo/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:/david/repo/javolution/javolution/5.5.1/javolution-5.5.1.jar:/david/repo/log4j/apache-log4j-extras/1.2.17/apache-log4j-extras-1.2.17.jar:/david/repo/org/antlr/antlr-runtime/3.4/antlr-runtime-3.4.jar:/david/repo/org/antlr/stringtemplate/3.2.1/stringtemplate-3.2.1.jar:/david/repo/antlr/antlr/2.7.7/antlr-2.7.7.jar:/david/repo/org/antlr/ST4/4.0.4/ST4-4.0.4.jar:/david/repo/org/apache/commons/commons-compress/1.4.1/commons-compress-1.4.1.jar:/david/repo/org/tukaani/xz/1.0/xz-1.0.jar:/david/repo/org/codehaus/jackson/jackson-core-asl/1.9.13/jackson-core-asl-1.9.13.jar:/david/repo/com/google/guava/guava/14.0.1/guava-14.0.1.jar:/david/repo/com/googlecode/javaewah/JavaEWAH/0.3.2/JavaEWAH-0.3.2.jar:/david/repo/org/iq80/snappy/snappy/0.2/snappy-0.2.jar:/david/repo/stax/stax-api/1.0.1/stax-api-1.0.1.jar:/david/repo/net/sf/opencsv/opencsv/2.3/opencsv-2.3.jar:/david/repo/org/spark-project/hive/hive-metastore/1.2.1.spark2/hive-metastore-1.2.1.spark2.jar:/david/repo/com/jolbox/bonecp/0.8.0.RELEASE/bonecp-0.8.0.RELEASE.jar:/david/repo/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/david/repo/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/david/repo/org/datanucleus/datanucleus-api-jdo/3.2.6/datanucleus-api-jdo-3.2.6.jar:/david/repo/org/datanucleus/datanucleus-rdbms/3.2.9/datanucleus-rdbms-3.2.9.jar:/david/repo/commons-pool/commons-pool/1.5.4/commons-pool-1.5.4.jar:/david/repo/commons-dbcp/commons-dbcp/1.4/commons-dbcp-1.4.jar:/david/repo/javax/jdo/jdo-api/3.0.1/jdo-api-3.0.1.jar:/david/repo/javax/transaction/jta/1.1/jta-1.1.jar:/david/repo/org/apache/avro/avro/1.7.7/avro-1.7.7.jar:/david/repo/com/thoughtworks/paranamer/paranamer/2.8/paranamer-2.8.jar:/david/repo/org/apache/avro/avro-mapred/1.7.7/avro-mapred-1.7.7-hadoop2.jar:/david/repo/org/apache/avro/avro-ipc/1.7.7/avro-ipc-1.7.7.jar:/david/repo/org/apache/avro/avro-ipc/1.7.7/avro-ipc-1.7.7-tests.jar:/david/repo/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:/david/repo/org/apache/calcite/calcite-avatica/1.2.0-incubating/calcite-avatica-1.2.0-incubating.jar:/david/repo/org/apache/calcite/calcite-core/1.2.0-incubating/calcite-core-1.2.0-incubating.jar:/david/repo/org/apache/calcite/calcite-linq4j/1.2.0-incubating/calcite-linq4j-1.2.0-incubating.jar:/david/repo/net/hydromatic/eigenbase-properties/1.1.5/eigenbase-properties-1.1.5.jar:/david/repo/org/apache/httpcomponents/httpclient/4.5.4/httpclient-4.5.4.jar:/david/repo/org/apache/httpcomponents/httpcore/4.4.8/httpcore-4.4.8.jar:/david/repo/org/codehaus/jackson/jackson-mapper-asl/1.9.13/jackson-mapper-asl-1.9.13.jar:/david/repo/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/david/repo/joda-time/joda-time/2.9.3/joda-time-2.9.3.jar:/david/repo/org/jodd/jodd-core/3.5.2/jodd-core-3.5.2.jar:/david/repo/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:/david/repo/org/datanucleus/datanucleus-core/3.2.10/datanucleus-core-3.2.10.jar:/david/repo/org/apache/thrift/libthrift/0.9.3/libthrift-0.9.3.jar:/david/repo/org/apache/thrift/libfb303/0.9.3/libfb303-0.9.3.jar:/david/repo/org/apache/derby/derby/10.12.1.1/derby-10.12.1.1.jar:/david/repo/org/scala-lang/scala-compiler/2.11.8/scala-compiler-2.11.8.jar:/david/repo/org/scala-lang/modules/scala-xml_2.11/1.0.4/scala-xml_2.11-1.0.4.jar:/david/repo/org/scalacheck/scalacheck_2.11/1.13.5/scalacheck_2.11-1.13.5.jar:/david/repo/org/scala-sbt/test-interface/1.0/test-interface-1.0.jar:/david/repo/org/spark-project/spark/unused/1.0.0/unused-1.0.0.jar:/david/repo/org/scalatest/scalatest_2.11/3.0.3/scalatest_2.11-3.0.3.jar:/david/repo/org/scalactic/scalactic_2.11/3.0.3/scalactic_2.11-3.0.3.jar:/david/repo/junit/junit/4.12/junit-4.12.jar:/david/repo/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/david/repo/com/novocode/junit-interface/0.11/junit-interface-0.11.jar org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner -s org.apache.spark.sql.hive.execution.HiveCatalogedDDLSuite -testName "SET LOCATION for managed table with partition" -C org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestReporter -showProgressMessages true
	Testing started at 9:39 PM ...
	05:39:18.230 WARN org.apache.spark.util.Utils: Your hostname, hadoop resolves to a loopback address: 127.0.0.1; using 10.229.51.168 instead (on interface eth0)
	05:39:18.231 WARN org.apache.spark.util.Utils: Set SPARK_LOCAL_IP if you need to bind to another address
	05:39:18.668 WARN org.apache.hadoop.util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
	05:39:26.256 WARN org.apache.hadoop.hive.metastore.ObjectStore: Version information not found in metastore. hive.metastore.schema.verification is not enabled so recording the schema version 1.2.0
	05:39:26.407 WARN org.apache.hadoop.hive.metastore.ObjectStore: Failed to get database default, returning NoSuchObjectException
	05:39:27.854 WARN org.apache.hadoop.hive.metastore.ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
	+----+----+
	|col2|col1|
	+----+----+
	+----+----+
	
	
	
	Results do not match for query:
	Timezone: sun.util.calendar.ZoneInfo[id="America/Los_Angeles",offset=-28800000,dstSavings=3600000,useDaylight=true,transitions=185,lastRule=java.util.SimpleTimeZone[id=America/Los_Angeles,offset=-28800000,dstSavings=3600000,useDaylight=true,startYear=0,startMode=3,startMonth=2,startDay=8,startDayOfWeek=1,startTime=7200000,startTimeMode=0,endMode=3,endMonth=10,endDay=1,endDayOfWeek=1,endTime=7200000,endTimeMode=0]]
	Timezone Env: 
	
	== Parsed Logical Plan ==
	'UnresolvedRelation `tbl_partition`
	
	== Analyzed Logical Plan ==
	col2: int, col1: int
	SubqueryAlias tbl_partition
	+- Relation[col2#20,col1#21] parquet
	
	== Optimized Logical Plan ==
	Relation[col2#20,col1#21] parquet
	
	== Physical Plan ==
	*FileScan parquet default.tbl_partition[col2#20,col1#21] Batched: true, Format: Parquet, Location: CatalogFileIndex[file:/tmp/warehouse-60f1b0e1-193c-49d7-b97c-99145466951b/tbl_partition], PartitionCount: 2, PartitionFilters: [], PushedFilters: [], ReadSchema: struct<col2:int>
	== Results ==
	
	== Results ==
	!== Correct Answer - 1 ==   == Spark Answer - 2 ==
	!struct<>                   struct<col2:int,col1:int>
	![33,2]                     [33,1]
	!                           [33,2]
	    
	       
	ScalaTestFailureLocation: org.apache.spark.sql.QueryTest at (QueryTest.scala:163)
	org.scalatest.exceptions.TestFailedException: 
	Results do not match for query:
	Timezone: sun.util.calendar.ZoneInfo[id="America/Los_Angeles",offset=-28800000,dstSavings=3600000,useDaylight=true,transitions=185,lastRule=java.util.SimpleTimeZone[id=America/Los_Angeles,offset=-28800000,dstSavings=3600000,useDaylight=true,startYear=0,startMode=3,startMonth=2,startDay=8,startDayOfWeek=1,startTime=7200000,startTimeMode=0,endMode=3,endMonth=10,endDay=1,endDayOfWeek=1,endTime=7200000,endTimeMode=0]]
	Timezone Env: 
	
	== Parsed Logical Plan ==
	'UnresolvedRelation `tbl_partition`
	
	== Analyzed Logical Plan ==
	col2: int, col1: int
	SubqueryAlias tbl_partition
	+- Relation[col2#20,col1#21] parquet
	
	== Optimized Logical Plan ==
	Relation[col2#20,col1#21] parquet
	
	== Physical Plan ==
	*FileScan parquet default.tbl_partition[col2#20,col1#21] Batched: true, Format: Parquet, Location: CatalogFileIndex[file:/tmp/warehouse-60f1b0e1-193c-49d7-b97c-99145466951b/tbl_partition], PartitionCount: 2, PartitionFilters: [], PushedFilters: [], ReadSchema: struct<col2:int>
	== Results ==
	
	== Results ==
	!== Correct Answer - 1 ==   == Spark Answer - 2 ==
	!struct<>                   struct<col2:int,col1:int>
	![33,2]                     [33,1]
	!                           [33,2]
	    
	       
		at org.scalatest.Assertions$class.newAssertionFailedException(Assertions.scala:528)
		at org.scalatest.FunSuite.newAssertionFailedException(FunSuite.scala:1560)
		at org.scalatest.Assertions$class.fail(Assertions.scala:1089)
		at org.scalatest.FunSuite.fail(FunSuite.scala:1560)
		at org.apache.spark.sql.QueryTest.checkAnswer(QueryTest.scala:163)
		at org.apache.spark.sql.QueryTest.checkAnswer(QueryTest.scala:169)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65$$anonfun$apply$mcV$sp$31$$anonfun$apply$mcV$sp$116.apply(DDLSuite.scala:1892)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65$$anonfun$apply$mcV$sp$31$$anonfun$apply$mcV$sp$116.apply(DDLSuite.scala:1874)
		at org.apache.spark.sql.test.SQLTestUtilsBase$class.withTempDir(SQLTestUtils.scala:215)
		at org.apache.spark.sql.execution.command.DDLSuite.withTempDir(DDLSuite.scala:137)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65$$anonfun$apply$mcV$sp$31.apply$mcV$sp(DDLSuite.scala:1874)
		at org.apache.spark.sql.test.SQLTestUtilsBase$class.withTable(SQLTestUtils.scala:273)
		at org.apache.spark.sql.execution.command.DDLSuite.withTable(DDLSuite.scala:137)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65.apply$mcV$sp(DDLSuite.scala:1873)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65.apply(DDLSuite.scala:1873)
		at org.apache.spark.sql.execution.command.DDLSuite$$anonfun$65.apply(DDLSuite.scala:1873)
		at org.scalatest.OutcomeOf$class.outcomeOf(OutcomeOf.scala:85)
		at org.scalatest.OutcomeOf$.outcomeOf(OutcomeOf.scala:104)
		at org.scalatest.Transformer.apply(Transformer.scala:22)
		at org.scalatest.Transformer.apply(Transformer.scala:20)
		at org.scalatest.FunSuiteLike$$anon$1.apply(FunSuiteLike.scala:186)
		at org.apache.spark.SparkFunSuite.withFixture(SparkFunSuite.scala:68)
		at org.scalatest.FunSuiteLike$class.invokeWithFixture$1(FunSuiteLike.scala:183)
		at org.scalatest.FunSuiteLike$$anonfun$runTest$1.apply(FunSuiteLike.scala:196)
		at org.scalatest.FunSuiteLike$$anonfun$runTest$1.apply(FunSuiteLike.scala:196)
		at org.scalatest.SuperEngine.runTestImpl(Engine.scala:289)
		at org.scalatest.FunSuiteLike$class.runTest(FunSuiteLike.scala:196)
		at org.apache.spark.sql.hive.execution.HiveCatalogedDDLSuite.org$scalatest$BeforeAndAfterEach$$super$runTest(HiveDDLSuite.scala:47)
		at org.scalatest.BeforeAndAfterEach$class.runTest(BeforeAndAfterEach.scala:221)
		at org.apache.spark.sql.hive.execution.HiveCatalogedDDLSuite.runTest(HiveDDLSuite.scala:47)
		at org.scalatest.FunSuiteLike$$anonfun$runTests$1.apply(FunSuiteLike.scala:229)
		at org.scalatest.FunSuiteLike$$anonfun$runTests$1.apply(FunSuiteLike.scala:229)
		at org.scalatest.SuperEngine$$anonfun$traverseSubNodes$1$1.apply(Engine.scala:396)
		at org.scalatest.SuperEngine$$anonfun$traverseSubNodes$1$1.apply(Engine.scala:384)
		at scala.collection.immutable.List.foreach(List.scala:381)
		at org.scalatest.SuperEngine.traverseSubNodes$1(Engine.scala:384)
		at org.scalatest.SuperEngine.org$scalatest$SuperEngine$$runTestsInBranch(Engine.scala:379)
		at org.scalatest.SuperEngine.runTestsImpl(Engine.scala:461)
		at org.scalatest.FunSuiteLike$class.runTests(FunSuiteLike.scala:229)
		at org.scalatest.FunSuite.runTests(FunSuite.scala:1560)
		at org.scalatest.Suite$class.run(Suite.scala:1147)
		at org.scalatest.FunSuite.org$scalatest$FunSuiteLike$$super$run(FunSuite.scala:1560)
		at org.scalatest.FunSuiteLike$$anonfun$run$1.apply(FunSuiteLike.scala:233)
		at org.scalatest.FunSuiteLike$$anonfun$run$1.apply(FunSuiteLike.scala:233)
		at org.scalatest.SuperEngine.runImpl(Engine.scala:521)
		at org.scalatest.FunSuiteLike$class.run(FunSuiteLike.scala:233)
		at org.apache.spark.SparkFunSuite.org$scalatest$BeforeAndAfterAll$$super$run(SparkFunSuite.scala:31)
		at org.scalatest.BeforeAndAfterAll$class.liftedTree1$1(BeforeAndAfterAll.scala:213)
		at org.scalatest.BeforeAndAfterAll$class.run(BeforeAndAfterAll.scala:210)
		at org.apache.spark.SparkFunSuite.run(SparkFunSuite.scala:31)
		at org.scalatest.tools.SuiteRunner.run(SuiteRunner.scala:45)
		at org.scalatest.tools.Runner$$anonfun$doRunRunRunDaDoRunRun$1.apply(Runner.scala:1340)
		at org.scalatest.tools.Runner$$anonfun$doRunRunRunDaDoRunRun$1.apply(Runner.scala:1334)
		at scala.collection.immutable.List.foreach(List.scala:381)
		at org.scalatest.tools.Runner$.doRunRunRunDaDoRunRun(Runner.scala:1334)
		at org.scalatest.tools.Runner$$anonfun$runOptionallyWithPassFailReporter$2.apply(Runner.scala:1011)
		at org.scalatest.tools.Runner$$anonfun$runOptionallyWithPassFailReporter$2.apply(Runner.scala:1010)
		at org.scalatest.tools.Runner$.withClassLoaderAndDispatchReporter(Runner.scala:1500)
		at org.scalatest.tools.Runner$.runOptionallyWithPassFailReporter(Runner.scala:1010)
		at org.scalatest.tools.Runner$.run(Runner.scala:850)
		at org.scalatest.tools.Runner.run(Runner.scala)
		at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.runScalaTest2(ScalaTestRunner.java:138)
		at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.main(ScalaTestRunner.java:28)
	
	
	Process finished with exit code 0
