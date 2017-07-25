package com.goldtek.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.goldtek.algorithm.Cost;
import com.goldtek.algorithm.Depot;

public class DbManager {
    private final static String DB_URL = "jdbc:mysql://192.168.56.29:3306/goldtek_dispatcher_cost?useUnicode=true&characterEncoding=utf8";
    private final static String DB_USER = "vrp";
    private final static String DB_PWD = "tsp";
    private static DbManager sInstance;
    
    private WorkerThread mWorker = null;
    private boolean mKeepConnection = true;
    private Connection mConnection = null;
    private Queue mQueue = null;

    public static DbManager getInstance() {
        if (sInstance == null) sInstance = new DbManager();
        return sInstance;
    }
    
    public synchronized boolean getPalmBox(IDbCallback<?> cb) {
        return mQueue.add(Message.obtain(cb, Action.GET_PALMBOX));
    }
    
    public synchronized boolean getGmapCost(IDbCallback<?> cb) {
        return mQueue.add(Message.obtain(cb, Action.GET_GMAPCOST));
    }
    
    private DbManager() {
        mQueue = new Queue();
        mWorker = new WorkerThread();
        mWorker.setDaemon(true);
        mWorker.start();
    }
    
    private class WorkerThread extends Thread {
        public void run() {
            while (mKeepConnection || !isInterrupted()) {
                Message msg = mQueue.get();
                try {
                    mConnection = getConnection();
                    if (!mConnection.isClosed()) {
                        switch (msg.getAction()) {
                        case GET_PALMBOX:
                            ((IDbCallback<Depot>)msg.from()).onQuery(executePalmBox(mConnection));
                            break;
                        case GET_GMAPCOST:
                            ((IDbCallback<Cost>)msg.from()).onQuery(executeGmapCost(mConnection));
                            break;
                        default:
                            break;
                        }
                    }
                    mConnection.close();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    msg.from().onQuery(null);
                }

            }
        }
    }
    
    private class Queue {
        private LinkedList<Message> mQueue = new LinkedList<Message>();
        synchronized boolean add(Message action) {
            if (mQueue.size() < 100) {
                mQueue.addLast(action);
                notifyAll();
                return true;
            }
            return false;
        }
        synchronized Message get() {
            while(mQueue.size() <= 0) {
                try {
                    wait();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message msg = mQueue.removeFirst();
            notifyAll();
            return msg;
        }
    }    
    
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.setLoginTimeout(2);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
    }
    
    private List<Depot> executePalmBox(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from palmbox");
        
        List<Depot> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Depot(String.valueOf(rs.getInt("ID")), rs.getString("County"), rs.getString("Area"),
                    rs.getString("Name"), rs.getDouble("Longitude"), rs.getDouble("Latitude")));
        }
        return list;
    }
    
    private List<Cost> executeGmapCost(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from gmap_cost");
        
        List<Cost> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Cost(rs.getInt("BoxFrom"), rs.getInt("BoxTo"), rs.getDouble("Distance")));
        }
        return list;
    }
}
