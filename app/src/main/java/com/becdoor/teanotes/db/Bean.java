package com.becdoor.teanotes.db;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-6-15
 * Time: 涓嬪崍4:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class Bean {
    protected Database db;
    public static final String DEFAULT_DATABASE="djtea.db";

    public Bean() {
        db = DatabaseManager.getInstance().openDatabase(getDatabaseName());
        createTable();
    }

    public String getDatabaseName() {
        return DEFAULT_DATABASE;
    }

    public Database getDatabase() {
        return db;
    }

    /**
     * Abstract create table method
     */
    public abstract void createTable();

}
