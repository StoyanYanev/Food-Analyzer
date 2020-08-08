package bg.sofia.uni.fmi.mjt.food.analyzer.utilitis;

public enum Commands {
    GET_FOOD("get-food"),
    GET_FOOD_REPORT("get-food-report"),
    GET_FOOD_BY_BARCODE("get-food-by-barcode"),
    FOOD_HELP("food");

    private String commandName;

    Commands(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}