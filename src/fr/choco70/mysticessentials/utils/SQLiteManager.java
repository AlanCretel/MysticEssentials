package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class SQLiteManager{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private Connection connection = null;

    public SQLiteManager() {
        openConnection();
        if(!tableExist("ME_PLAYERS")){
            createPlayersTable();
        }
        if(!tableExist("ME_HOMES")){
            createHomesTable();
        }
        if(!tableExist("ME_LAST_LOCATIONS")){
            createLastLocationsTable();
        }
        if(!tableExist("ME_WARPS")){
            createWarpsTable();
        }
        if(!tableExist("ME_LAST_DEATHS")){
            createLastDeathsTable();
        }
        if(!tableExist("ME_IGNORED_PLAYERS")){
            createIgnoredPlayersTable();
        }
    }

    public void openConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "MysticEssentials.db");
            System.out.println(ChatColor.GREEN + "[MysticEssentials] Successfully connected to database.");
        } catch (Exception e) {
            System.out.println(ChatColor.RED + "[MysticEssentials] Error when setting up connection with database.");
        }
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(ChatColor.RED + "[MysticEssentials] Error when closing connection with database.");
        }
    }

    public void createPlayersTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_PLAYERS " +
                    "(UUID TEXT PRIMARY KEY NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " DISPLAY_NAME TEXT NOT NULL," +
                    " LOCALE TEXT NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createHomesTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_HOMES " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " UUID TEXT NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " IS_DEFAULT BOOLEAN DEFAULT TRUE NOT NULL," +
                    " WORLD TEXT NOT NULL," +
                    " X DOUBLE NOT NULL," +
                    " Y DOUBLE NOT NULL," +
                    " Z DOUBLE NOT NULL," +
                    " PITCH FLOAT NOT NULL," +
                    " YAW FLOAT NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createLastLocationsTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_LAST_LOCATIONS " +
                    "(UUID TEXT PRIMARY KEY NOT NULL," +
                    " WORLD TEXT NOT NULL," +
                    " X DOUBLE NOT NULL," +
                    " Y DOUBLE NOT NULL," +
                    " Z DOUBLE NOT NULL," +
                    " PITCH FLOAT NOT NULL," +
                    " YAW FLOAT NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWarpsTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_WARPS " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " REQUIRES_PERMISSION BOOLEAN DEFAULT FALSE NOT NULL," +
                    " PERMISSION TEXT," +
                    " WORLD TEXT NOT NULL," +
                    " X DOUBLE NOT NULL," +
                    " Y DOUBLE NOT NULL," +
                    " Z DOUBLE NOT NULL," +
                    " PITCH FLOAT NOT NULL," +
                    " YAW FLOAT NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createLastDeathsTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_LAST_DEATHS " +
                    "(UUID STRING PRIMARY KEY NOT NULL," +
                    " WORLD TEXT NOT NULL," +
                    " X DOUBLE NOT NULL," +
                    " Y DOUBLE NOT NULL," +
                    " Z DOUBLE NOT NULL," +
                    " PITCH FLOAT NOT NULL," +
                    " YAW FLOAT NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIgnoredPlayersTable(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ME_IGNORED_PLAYERS " +
                    "(UUID TEXT PRIMARY KEY NOT NULL," +
                    " IGNORED_UUID TEXT NOT NULL," +
                    " IGNORED BOOLEAN DEFAULT TRUE NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTables(){
        ArrayList<String> tables = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'");
            while(results.next()){
                tables.add(results.getString("name"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public boolean tableExist(String tableName){
        return getTables().contains(tableName);
    }

    public void insertPlayer(UUID uuid, String name, String displayName, String locale){
        try{
            connection.setAutoCommit(true);

            String sql = "INSERT INTO ME_PLAYERS (UUID,NAME,DISPLAY_NAME,LOCALE) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, displayName);
            preparedStatement.setString(4, locale);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to insert player " + name + " into database.");
            e.printStackTrace();
        }
    }

    public boolean playerExist(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_PLAYERS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayerDisplayName(UUID uuid){
        try{
            String sql = "SELECT DISPLAY_NAME FROM ME_PLAYERS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("DISPLAY_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plugin.getServer().getOfflinePlayer(uuid).getName();
    }

    public String getPlayerName(UUID uuid){
        try{
            String sql = "SELECT NAME FROM ME_PLAYERS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plugin.getServer().getOfflinePlayer(uuid).getName();
    }

    public void insertHome(UUID uuid, String name, boolean isDefault, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "INSERT INTO ME_HOMES (UUID,NAME,IS_DEFAULT,WORLD,X,Y,Z,PITCH,YAW) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setBoolean(3, isDefault);
            preparedStatement.setString(4, location.getWorld().getName());
            preparedStatement.setDouble(5, location.getX());
            preparedStatement.setDouble(6, location.getY());
            preparedStatement.setDouble(7, location.getZ());
            preparedStatement.setFloat(8, location.getPitch());
            preparedStatement.setFloat(9, location.getYaw());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to insert home " + name + " into database.");
            e.printStackTrace();
        }
    }

    public boolean homeExist(UUID uuid, String name){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDefaultHomeName(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE UUID=? AND IS_DEFAULT";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Location getDefaultHome(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE UUID=? AND IS_DEFAULT";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String world = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float pitch = resultSet.getFloat("PITCH");
                float yaw = resultSet.getFloat("YAW");

                return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDefaultHome(UUID uuid, String homeName){
        try{
            connection.setAutoCommit(true);

            String sql1 = "UPDATE ME_HOMES SET IS_DEFAULT=false WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, uuid.toString());
            preparedStatement1.setString(2, homeName);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "UPDATE ME_HOMES SET IS_DEFAULT=? WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(3, homeName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to update home " + homeName + " into database.");
            e.printStackTrace();
        }
    }

    public Location getHomeLocation(UUID uuid, String name){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String world = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float pitch = resultSet.getFloat("PITCH");
                float yaw = resultSet.getFloat("YAW");
                return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateHome(UUID uuid, String name, boolean isDefault, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "UPDATE ME_HOMES SET IS_DEFAULT=?, WORLD=?, X=?, Y=?, Z=?, PITCH=?,YAW=? WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, isDefault);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setFloat(6, location.getPitch());
            preparedStatement.setFloat(7, location.getYaw());
            preparedStatement.setString(8, uuid.toString());
            preparedStatement.setString(9, name);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to update home " + name + " into database.");
            e.printStackTrace();
        }
    }

    public void updateHome(UUID uuid, String name, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "UPDATE ME_HOMES SET WORLD=?, X=?, Y=?, Z=?, PITCH=?,YAW=? WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setFloat(5, location.getPitch());
            preparedStatement.setFloat(6, location.getYaw());
            preparedStatement.setString(7, uuid.toString());
            preparedStatement.setString(8, name);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to update home " + name + " into database.");
            e.printStackTrace();
        }
    }

    public void removeHome(UUID uuid, String name){
        try{
            connection.setAutoCommit(true);

            String sql = "DELETE FROM ME_HOMES WHERE UUID=? AND NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to remove home " + name + " from database.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getHomes(UUID uuid){
        ArrayList<String> homes = new ArrayList<>();
        try{
            String sql = "SELECT NAME FROM ME_HOMES WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                homes.add(resultSet.getString("NAME"));
            }
            return homes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    public boolean haveHome(UUID uuid, String name){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE NAME=? AND UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean haveDefaultHome(UUID uuid, String name){
        try{
            String sql = "SELECT * FROM ME_HOMES WHERE NAME=? AND UUID=? AND IS_DEFAULT";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayerLocale(UUID playerUUID){
        try{
            String sql = "SELECT LOCALE FROM ME_PLAYERS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, playerUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "en_en";
    }

    public void updatePlayerLocale(UUID playerUUID, String locale){
        try{
            connection.setAutoCommit(true);
            String sql = "UPDATE ME_PLAYERS SET LOCALE=? WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, locale);
            preparedStatement.setString(2, playerUUID.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertWarp(String name, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "INSERT INTO ME_WARPS (NAME, WORLD, X, Y, Z, PITCH, YAW) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setFloat(6, location.getPitch());
            preparedStatement.setFloat(7, location.getYaw());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to insert warp " + name + " into database.");
            e.printStackTrace();
        }
    }


    public void updateWarp(String name, Location location){
        try{
            connection.setAutoCommit(true);
            String sql = "UPDATE ME_WARPS SET WORLD=?, X=?, Y=?, Z=?, PITCH=?, YAW=? WHERE NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setFloat(5, location.getPitch());
            preparedStatement.setFloat(6, location.getYaw());
            preparedStatement.setString(7, name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean warpExist(String name){
        try{
            String sql = "SELECT * FROM ME_WARPS WHERE NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getWarps(){
        ArrayList<String> warps = new ArrayList<>();
        try{
            String sql = "SELECT NAME FROM ME_WARPS";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                warps.add(resultSet.getString("NAME"));
            }
            return warps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warps;
    }

    public Location getWarpLocation(String warpName){
        try{
            String sql = "SELECT * FROM ME_WARPS WHERE NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, warpName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String worldName = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float pitch = resultSet.getFloat("PITCH");
                float yaw = resultSet.getFloat("YAW");
                return new Location(plugin.getServer().getWorld(worldName),x,y,z,yaw,pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeWarp(String warpName){
        try{
            connection.setAutoCommit(true);
            String sql = "DELETE FROM ME_WARPS WHERE NAME=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, warpName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void setLastLocation(UUID uuid, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "INSERT INTO ME_LAST_LOCATIONS (UUID, WORLD, X, Y, Z, PITCH, YAW) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setFloat(6, location.getPitch());
            preparedStatement.setFloat(7, location.getYaw());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to insert last location for " + plugin.getServer().getOfflinePlayer(uuid).getName() + " into database.");
            e.printStackTrace();
        }
    }

    public void updateLastLocation(UUID uuid, Location location){
        try{
            connection.setAutoCommit(true);
            String sql = "UPDATE ME_LAST_LOCATIONS SET WORLD=?, X=?, Y=?, Z=?, PITCH=?, YAW=? WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setFloat(5, location.getPitch());
            preparedStatement.setFloat(6, location.getYaw());
            preparedStatement.setString(7, uuid.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Location getLastLocation(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_LAST_LOCATIONS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String worldName = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float pitch = resultSet.getFloat("PITCH");
                float yaw = resultSet.getFloat("YAW");
                return new Location(plugin.getServer().getWorld(worldName),x,y,z,yaw,pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean haveLastLocation(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_LAST_LOCATIONS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setLastDeath(UUID uuid, Location location){
        try{
            connection.setAutoCommit(true);

            String sql = "INSERT INTO ME_LAST_DEATHS (UUID, WORLD, X, Y, Z, PITCH, YAW) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setFloat(6, location.getPitch());
            preparedStatement.setFloat(7, location.getYaw());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            System.out.println(ChatColor.RED + "[MysticEssentials] Unable to insert last location for " + plugin.getServer().getOfflinePlayer(uuid).getName() + " into database.");
            e.printStackTrace();
        }
    }

    public void updateLastDeath(UUID uuid, Location location){
        try{
            connection.setAutoCommit(true);
            String sql = "UPDATE ME_LAST_DEATHS SET WORLD=?, X=?, Y=?, Z=?, PITCH=?, YAW=? WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setFloat(5, location.getPitch());
            preparedStatement.setFloat(6, location.getYaw());
            preparedStatement.setString(7, uuid.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Location getLastDeath(UUID uuid){
        try{
            String sql = "SELECT * FROM ME_LAST_DEATHS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String worldName = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float pitch = resultSet.getFloat("PITCH");
                float yaw = resultSet.getFloat("YAW");
                return new Location(plugin.getServer().getWorld(worldName),x,y,z,yaw,pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<UUID> getIgnoredPlayers(UUID uuid){
        ArrayList<UUID> ignored = new ArrayList<>();
        try{
            String sql = "SELECT IGNORED_UUID FROM ME_IGNORED_PLAYERS WHERE UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ignored.add(UUID.fromString(resultSet.getString("IGNORED_UUID")));
            }
            return ignored;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean doesIgnorePlayer(UUID uuid, UUID ignoredUUID){
        try{
            String sql = "SELECT * FROM ME_IGNORED_PLAYERS WHERE UUID=? AND IGNORED_UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, ignoredUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setIgnorePlayer(UUID uuid, UUID ignoredUUID){
        try{
            connection.setAutoCommit(true);
            String sql = "INSERT INTO ME_IGNORED_PLAYERS (UUID, IGNORED_UUID) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, ignoredUUID.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void removeIgnoredPlayer(UUID uuid, UUID ignoredUUID){
        try{
            connection.setAutoCommit(true);
            String sql = "DELETE FROM ME_IGNORED_PLAYERS WHERE UUID=? AND IGNORED_UUID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, ignoredUUID.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
