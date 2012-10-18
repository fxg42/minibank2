@Grab('org.mortbay.jetty:jetty-embedded:6.1.26')
@Grab('org.apache.derby:derby:10.9.1.0')
@Grab('commons-dbcp:commons-dbcp:1.4')

import groovy.servlet.*
import groovy.sql.*
import java.util.logging.*
import models.*
import org.apache.commons.dbcp.*
import org.mortbay.jetty.*
import org.mortbay.jetty.handler.*
import org.mortbay.jetty.servlet.*

def logger = Logger.getLogger(this.getClass().getCanonicalName())

def dataSource = new BasicDataSource()
dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver")
dataSource.setUrl("jdbc:derby:derby_data;create=true")

def sql = new Sql(dataSource.connection)
try {
  try {
    sql.executeUpdate("drop table people")
  } catch (e) {
    logger.info("Table 'people' does not exist. Creating it...")
  }
  sql.executeUpdate("create table people(id integer not null generated always as identity (start with 1, increment by 1), name varchar(255), constraint pkey primary key (id))")
  sql.executeUpdate("insert into people (name) values ('Alice'), ('Bob'), ('Carol'), ('Dave')")
} finally {
  sql?.close()
}

Person.dataSource = dataSource

def server = new Server(8090)

def groovlet = new GroovyServlet() {
  @Override protected GroovyScriptEngine createGroovyScriptEngine() {
    def gse = new GroovyScriptEngine(this, this.class.classLoader)
    gse.config.sourceEncoding = "UTF-8" // force UTF-8 encoding when evaluating
    gse
  }

  @Override protected void setVariables(ServletBinding binding) {
    binding.setVariable('dataSource', dataSource)
  }
}

// Expose files in the '/public' folder at 'localhost:8090/*'
def staticContext = new Context(server, "/")
staticContext.setHandler(new ResourceHandler())
staticContext.setResourceBase("./public")

// Create servlet context and expose the router at 'localhost:8090/person/*'
def root = new Context(server, "/", Context.SESSIONS)
root.addServlet(new ServletHolder(groovlet), "/person/*")
root.setResourceBase("./controllers")

server.start()
server.join()
