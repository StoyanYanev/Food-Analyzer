package bg.sofia.uni.fmi.mjt.food.analyzer.utilitis;

public final class CommandInformation {
    private static final String GET_FOOD_VALUE_TEMPLATE = " <food_name>";
    private static final String GET_FOOD_REPORT_VALUE_TEMPLATE = " <food_fdcId>";
    private static final String GET_FOOD_BY_BAR_CODE_VALUE_TEMPLATE = " --code=<gtinUpc_code>" + "|--img=<barcode_image_file>";

    public static final String COMMAND_INFORMATION = getCommandInformationInstance();

    private CommandInformation() {

    }

    private static String getCommandInformationInstance() {
        final StringBuilder commandInformation = new StringBuilder();

        commandInformation.append(Commands.GET_FOOD.getCommandName())
                .append(GET_FOOD_VALUE_TEMPLATE)
                .append(" Gives information about the product(description, fdcId, gtinUpc)")
                .append(System.lineSeparator())
                .append(Commands.GET_FOOD_REPORT.getCommandName())
                .append(GET_FOOD_REPORT_VALUE_TEMPLATE)
                .append(" Gives information about the name, nutrients, and ingredients of the product")
                .append(System.lineSeparator())
                .append(Commands.GET_FOOD_BY_BARCODE.getCommandName())
                .append(GET_FOOD_BY_BAR_CODE_VALUE_TEMPLATE)
                .append(" Gives information about the product by given barcode")
                .append(System.lineSeparator());

        return commandInformation.toString();
    }
}