package models

import groovy.sql.*
import groovy.transform.*

@Canonical class Person {
  def id, name
  static dataSource

  static create (name) {
    return new Person(name:name)
  }
  
  static findAll () {
    withSql { sql ->
      sql.rows("select id, name from people order by id").collect { row ->
        new Person(row.id, row.name)
      }
    }
  }

  static findById (id) {
    withSql { sql ->
      row = sql.firstRow("select id, name from people where id = ?", [id])
      new Person(row.id, row.name)
    }
  }

  def save () {
    withSql { sql ->
      sql.executeUpdate('update people set name = ? where id = ?', [name, id])
    }
  }

  def remove () {
    withSql { sql ->
      sql.executeUpdate("delete from people where id = ?", [id])
    }
  }

  static withSql (fn) {
    def sql = new Sql(dataSource.connection)
    try {
      return fn(sql)
    } finally {
      sql?.close()
    }
  }
}
