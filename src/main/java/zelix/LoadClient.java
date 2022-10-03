package zelix;

import zelix.utils.*;

public class LoadClient
{
    public static boolean isCheck;
    
    public static void RLoad(final String string, final String string2) {
        try {
            LoadClient.isCheck = true;
            final Class Main = Class.forName(string);
            Main.newInstance();
        }
        catch (IllegalAccessException e) {
            LoadClient.isCheck = false;
            new Cr4sh();
        }
        catch (InstantiationException e2) {
            LoadClient.isCheck = false;
            new Cr4sh();
        }
        catch (ClassNotFoundException e3) {
            LoadClient.isCheck = false;
            Class Main2 = null;
            try {
                LoadClient.isCheck = true;
                Main2 = Class.forName(string2);
                Main2.newInstance();
            }
            catch (ClassNotFoundException ex) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
            catch (IllegalAccessException ex2) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
            catch (InstantiationException ex3) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
        }
    }
    
    static {
        LoadClient.isCheck = false;
    }
}
