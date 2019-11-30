package com.udf.udf.controller;



import com.udf.udf.controller.dto.metadata.ColumnMetadata;
import com.udf.udf.controller.dto.metadata.TableMetaData;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
// controller class
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    EntityManagerFactory emf;




    @RequestMapping(value = "/abc")
    public String test() {
//
        //Session session = entityManager.unwrap(org.hibernate.Session.class)
        String query = "CREATE TABLE CUSTOMERS(\n" +
                "   ID   INT              NOT NULL,\n" +
                "   NAME VARCHAR (20)     NOT NULL,\n" +
                "   AGE  INT              NOT NULL,\n" +
                "   ADDRESS  CHAR (25) ,\n" +
                "   SALARY   DECIMAL (18, 2),       \n" +
                "   PRIMARY KEY (ID)\n" +
                ")";
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        int temp
                = em.createNativeQuery(query).executeUpdate();
        tx.commit();

        // entityManager.getTransaction().commit();
        // int temp=entityManager.createNativeQuery("SELECT GETDATE()").executeUpdate();
        return "updated     " + temp;


    }


    @RequestMapping(value = "/temp/{tablename}")
    public String clearTable(@PathVariable String tablename) {
        EntityTransaction txn = null;

        String msg = null;
        try {
            EntityManager entityManager = emf.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();

            String sql = "CREATE TABLE Persons (\n" +
                    "    PersonID int,\n" +
                    "    LastName varchar(255),\n" +
                    "    FirstName varchar(255),\n" +
                    "    Address varchar(255),\n" +
                    "    City varchar(255)\n" +
                    ")";
            entityManager.createNativeQuery(sql).executeUpdate();

            txn.commit();
            msg = "table is created successfully " + tablename;
        } catch (Throwable e) {
            msg = "table is not created " + tablename;
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;

        }
        return msg;

    }


    @RequestMapping(value = "/metadata/{tablename}")
    public TableMetaData getmetadata(@PathVariable String tablename) {
        EntityTransaction txn = null;

        String msg=null;
        List<ColumnMetadata> list=new ArrayList<>();
        TableMetaData tableMetaData=new TableMetaData();
        try {
            EntityManager entityManager = emf.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();

            String sql="SELECT *\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE table_name = 'PERSONS'";
            Session session = entityManager.unwrap(org.hibernate.Session.class);

            List<Object[]> employees = session.createNativeQuery(sql).list();


            tableMetaData.setTableName(tablename);
            for ( Object[] employee:employees) {


                ColumnMetadata columnMetadata=new ColumnMetadata();

                 String name =(String)employee[3];
                Integer pos =(Integer)employee[4];
                //String nullable =(String)employee[10];
                String type =(String)employee[21];
                columnMetadata.setColumnName(name);
                columnMetadata.setColumnOrder(pos);
                columnMetadata.setColumnType(type);list.add(columnMetadata);

            }
           tableMetaData.setColumnMetadataList(list);
            txn.commit();
            msg= "table is created successfully "+tablename;
        }
        catch ( Throwable e ) {
            msg= "table is not created "+tablename;
            if ( txn != null && txn.isActive() ) {
                txn.rollback();
            }
            throw e;

        }

        return tableMetaData ;

    }


}