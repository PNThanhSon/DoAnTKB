<?xml version="1.0" encoding="UTF-8"?>

        <?import javafx.geometry.*?>
        <?import javafx.scene.control.*?>
        <?import javafx.scene.layout.*?>
        <?import javafx.scene.text.*?>

<ScrollPane fitToWidth="true" xmlns="http://javafx.com/javafx/17.0.12" xmlns:fx="http://javafx.com/fxml/1" fx:controller="controllers.QuanLyGiaoVien.ThemGVController">
    <content>
        <VBox spacing="10.0">
            <padding>
                <Insets bottom="20" left="20" right="20" top="20" />
            </padding>
            <GridPane hgap="10.0" maxWidth="400" vgap="10.0">
                <columnConstraints>
                    <ColumnConstraints halignment="RIGHT" />            <!-- Cột label -->
                    <ColumnConstraints percentWidth="50" />              <!-- Cột input -->
                    <ColumnConstraints hgrow="ALWAYS" percentWidth="50" />

                </columnConstraints>


                <Label text="Họ GV:" GridPane.columnIndex="0" GridPane.rowIndex="1" />
                <TextField text = "" fx:id="txtHoGV" GridPane.columnIndex="1" GridPane.rowIndex="1" promptText="Bắt Buộc..." />
                <Text style="-fx-fill: red;" fx:id="tbHoGV"  GridPane.columnIndex="2" GridPane.rowIndex="1" text=""/>

                <Label text="Tên GV:" GridPane.columnIndex="0" GridPane.rowIndex="2" />
                <TextField text = "" fx:id="txtTenGV" GridPane.columnIndex="1" GridPane.rowIndex="2" promptText="Bắt Buộc..."/>
                <Text style="-fx-fill: red;" fx:id="tbTenGV"  GridPane.columnIndex="2" GridPane.rowIndex="2" text=""/>

                <Label text="Giới tính:" GridPane.columnIndex="0" GridPane.rowIndex="3" />
                <ComboBox fx:id="comboGioiTinh" layoutX="100" layoutY="150" prefWidth="220" GridPane.columnIndex="1" GridPane.rowIndex="3" />
                <Text style="-fx-fill: red;" fx:id="tbGioiTinh"  GridPane.columnIndex="2" GridPane.rowIndex="3" text=""/>

                <Label text="Chuyên môn:" GridPane.columnIndex="0" GridPane.rowIndex="4" />
                <TextField text = "" fx:id="txtChuyenMon" GridPane.columnIndex="1" GridPane.rowIndex="4" promptText="Trình độ chuyên môn..." />
                <Text style="-fx-fill: red;" fx:id="tbChuyenMon"  GridPane.columnIndex="2" GridPane.rowIndex="4" text=""/>

                <Label text="Tổ chuyên môn" GridPane.columnIndex="0" GridPane.rowIndex="5" />
                <ComboBox fx:id="comboToChuyenMon" layoutX="100" layoutY="150" prefWidth="220" GridPane.columnIndex="1" GridPane.rowIndex="5" />
                <Text style="-fx-fill: red;" fx:id="tbMaTCM"  GridPane.columnIndex="2" GridPane.rowIndex="5" text=""/>

                <Label text="Số tiết quy định:" GridPane.columnIndex="0" GridPane.rowIndex="6" />
                <TextField text = "" fx:id="txtSoTietQD" GridPane.columnIndex="1" GridPane.rowIndex="6" promptText="0-9..."/>
                <Text style="-fx-fill: red;" fx:id="tbSoTietQD"  GridPane.columnIndex="2" GridPane.rowIndex="6" text=""/>

                <Label text="Số tiết thực hiện:" GridPane.columnIndex="0" GridPane.rowIndex="7" />
                <TextField text = "" fx:id="txtSoTietTH" GridPane.columnIndex="1" GridPane.rowIndex="7" promptText="0-9..."/>
                <Text style="-fx-fill: red;" fx:id="tbSoTietTH"  GridPane.columnIndex="2" GridPane.rowIndex="7" text=""/>

                <Label text="Số tiết dư thiếu:" GridPane.columnIndex="0" GridPane.rowIndex="8" />
                <TextField text = "" fx:id="txtSoTietDT" GridPane.columnIndex="1" GridPane.rowIndex="8" promptText="0-9..."/>
                <Text style="-fx-fill: red;" fx:id="tbSoTietDT"  GridPane.columnIndex="2" GridPane.rowIndex="8" text=""/>

                <Label text="Email:" GridPane.columnIndex="0" GridPane.rowIndex="9" />
                <TextField text = "" fx:id="txtEmail" GridPane.columnIndex="1" GridPane.rowIndex="9" promptText="ten@gmail.com" />
                <Text style="-fx-fill: red;" fx:id="tbEmail"  GridPane.columnIndex="2" GridPane.rowIndex="9" text=""/>

                <Label text="SĐT:" GridPane.columnIndex="0" GridPane.rowIndex="10" />
                <TextField text = "" fx:id="txtSDT" GridPane.columnIndex="1" GridPane.rowIndex="10" promptText="0-9..."/>
                <Text style="-fx-fill: red;" fx:id="tbSDT"  GridPane.columnIndex="2" GridPane.rowIndex="10" text=""/>

                <Label text="Mật khẩu:" GridPane.columnIndex="0" GridPane.rowIndex="11" />
                <PasswordField fx:id="txtMatKhau" GridPane.columnIndex="1" GridPane.rowIndex="11" promptText="Bắt Buộc..."/>
                <Text style="-fx-fill: red;" fx:id="tbMatKhau"  GridPane.columnIndex="2" GridPane.rowIndex="11" text=""/>

                <Label text="Ghi chú:" GridPane.columnIndex="0" GridPane.rowIndex="12" />
                <TextArea fx:id="txtGhiChu"  GridPane.columnIndex="1" GridPane.rowIndex="12"  prefRowCount="2" promptText="Không yêu cầu..."/>
                <Text style="-fx-fill: red;" fx:id="tbGhiChu"  GridPane.columnIndex="2" GridPane.rowIndex="12" text=""/>

                <rowConstraints>
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                    <RowConstraints />
                </rowConstraints>

            </GridPane>

            <HBox alignment="CENTER_RIGHT" spacing="10.0">
                <Button onAction="#handleThem" text="Lưu" />
                <Button onAction="#handleHuy" text="Hủy" />
            </HBox>
        </VBox>
    </content>
</ScrollPane>