package com.example.miyah
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException


class mysqlTest{
object mysqlTest {
//    ?serverTimezone=UTC

    fun connection(){
        try {
            val c = DriverManager.getConnection(
                "jdbc:mysql://localhost/miyah1",
                "root",
                "Pass@1234"

            )
            println(" connect")
            val statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
            //query test to check if the connection is made
            val result = statement.executeQuery("SELECT * FROM `users` WHERE id = 3")

            //result.next has to be here, idk why but if you remove it the code won't work :/

            result.next()

            //just to print the first name from the query, the other columns are  : id, Lname, email, password, waterlevel, and phonenum

           val strTest = result.getString("Fname")
            println(strTest)

        }catch (e: SQLException){
            println(e)

            print("couldn't connect")
        }


    }

}
}