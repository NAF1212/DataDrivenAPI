package config;

public enum Environment {

	INT,
    DEV,
    UAT;
    

    public static Environment getEnv() {

        String env = System.getProperty("env");

        if (env == null) {
            System.out.println("No environment passed. Defaulting to INT");
            return INT;
        }
        

        return Environment.valueOf(env.toUpperCase());
    }
}