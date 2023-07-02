package com.github.sukruth.rca;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RCAGui {
    JFrame getInputFrame;
    JLabel Label1;
    JButton nextButton;
    JTextField fieldA;
    JTextField fieldB;
    JLabel Label2;
    JFrame frameOne;
    Adder calculateAdderSum;

    public RCAGui() {
        frameOne = new JFrame("Ripple Carry Adder");
        frameOne.setLayout(new BorderLayout(4, 4));
        frameOne.setDefaultCloseOperation(frameOne.EXIT_ON_CLOSE);
        Label1 = new JLabel("   Implementing 32-bit Ripple Carry Adder  ");
        Label2 = new JLabel("Please hit the next button");
        nextButton = new JButton("Next");
        JPanel PanelOne = new JPanel();
        PanelOne.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add left spacing

        frameOne.add(PanelOne, BorderLayout.WEST);
        frameOne.add(Label1, BorderLayout.NORTH);
        frameOne.add(Label2, BorderLayout.CENTER);
        frameOne.add(nextButton, BorderLayout.PAGE_END);

        frameOne.setSize(480, 200);
        frameOne.setVisible(true);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameOne.dispose();
                InputFrame(); // this method when called the second frame appear
            }
        });
        frameOne.setLocationRelativeTo(null);
        frameOne.getRootPane().setDefaultButton(nextButton); // Set default button for "Enter" key

    }

    void InputFrame() {
        getInputFrame = new JFrame("Calculate Sum");
        getInputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getInputFrame.setSize(480, 200);
        getInputFrame.setLayout(new BorderLayout());

        // Create and add number fields
        fieldA = new JTextField(10);
        fieldB = new JTextField(10);

        JLabel label = new JLabel("Give inputs A & B to Calculate Sum");
        JLabel labelA = new JLabel(" Number A : ");
        JLabel labelB = new JLabel(" Number B : ");
        getInputFrame.add(fieldA);
        getInputFrame.add(fieldB);
        getInputFrame.setSize(480, 200);

        // Create and add calculate button
        JButton calculateButton = new JButton("Calculate Sum");
        JPanel Panel1 = new JPanel();
        JPanel Panel2 = new JPanel();
        Panel1.setLayout(new GridLayout(2, 4, 15, 5));
        Panel2.setLayout(new GridLayout(2, 4, 15, 20));
        Panel1.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add left spacing
        Panel2.setBorder(new EmptyBorder(10, 10, 0, 0)); // Add left spacing


        Panel1.add(labelA);
        Panel1.add(labelB);

        Panel2.add(createSmallerTextField(fieldA)); // Use custom method to create smaller text field
        Panel2.add(createSmallerTextField(fieldB)); // Use custom method to create smaller text field

        // Set smaller preferred size for the text fields
        Font smallFont = new Font(fieldA.getFont().getName(), fieldB.getFont().getStyle(), 14);
        fieldA.setFont(smallFont);
        fieldB.setFont(smallFont);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);

        getInputFrame.add(label, BorderLayout.NORTH);
        getInputFrame.add(Panel1, BorderLayout.WEST);
        getInputFrame.add(Panel2, BorderLayout.CENTER);
        getInputFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to the calculate button
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSum(fieldA, fieldB);
            }
        });

        getInputFrame.getRootPane().setDefaultButton(calculateButton); // Set default button for "Enter" key
        getInputFrame.setLocationRelativeTo(null);
        getInputFrame.setVisible(true);
    }
    private JTextField createSmallerTextField(JTextField textField) {
        // Create a custom border to adjust the appearance of the text field
        Border originalBorder = textField.getBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
        Border compoundBorder = BorderFactory.createCompoundBorder(originalBorder, emptyBorder);
        textField.setBorder(compoundBorder);
        return textField;
    }

    long numberA;
    long numberB;

    public void strToInt(JTextField textA, JTextField textB) {
        // Parse the values as integers
        numberA = Long.parseLong(textA.getText());
        numberB = Long.parseLong(textB.getText());
    }

    PrintArray printArrayObj = new PrintArray();
    int[] aArr;
    int[] bArr;

    private void calculateSum(JTextField txtA, JTextField txtB) {
        calculateAdderSum = new Adder();

        try {
            strToInt(fieldA, fieldB);
            // Check if the numbers are within the allowed range
            if (numberA >= 0 && numberB >= 0 && numberA <= 4294967295L && numberB <= 4294967295L) {
                // Calculate the sum
                long longSum = numberA + numberB;
                aArr = calculateAdderSum.convertIntToBool(numberA);
                bArr = calculateAdderSum.convertIntToBool(numberB);
                //calculateAdderSum.calculateRippleCarryAdder(calculateAdderSum.a, calculateAdderSum.b);
                calculateAdderSum.calculateRippleCarryAdder(aArr,bArr);

                StringBuilder message = new StringBuilder();
                message.append("    A : ").append(printArrayObj.displayArray(aArr)).append("\n");
                message.append("    B : ").append(printArrayObj.displayArray(bArr)).append("\n");
                message.append("Sum: ").append(printArrayObj.displayArray(calculateAdderSum.sum)).append("\n");
                message.append("Lcout: ").append(calculateAdderSum.c).append("\n");
                message.append("Delay: ").append(calculateAdderSum.Dmax).append("D").append("\n");

                String s = message.toString();

                // Display the result in a dialog box
                JOptionPane.showMessageDialog(null, s, "Result", JOptionPane.INFORMATION_MESSAGE);

            } else {
                // Display an error message if the numbers are out of range
                JOptionPane.showMessageDialog(null, "Numbers must be between 0 and 4294967295.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            // Display an error message if the input is not a valid number
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RCAGui();
            }
        });
    }
}


class Adder {
    IntToBin intToBinObj;
    RippleCarryAdder_32 rca;
    public int[] arr;
    //public int[] b;
    int[] sum;
    int c;
    int D;
    int Dmax;
    RCAGui rcaGUI;

    public int[] convertIntToBool(long x) {

        intToBinObj = new IntToBin();

        Thread threadA = new Thread(() -> {
            arr = intToBinObj.intToBoolArray(x);
        });

        // Thread threadB = new Thread(() -> {
        //     b = intToBinObj.intToBoolArray(y);
        // });

        threadA.start();
        //threadB.start();

        try {
            threadA.join();
            //threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public void calculateRippleCarryAdder(int[] a_arr, int[] b_arr) {
        rca = new RippleCarryAdder_32();
        sum = new int[32];
        c = 0; // Assuming carryIn = 0
        D = 0;
        Dmax = 0;
        for (int i = 0; i < 32; i++) {
            sum[i] = (a_arr[i] ^ b_arr[i] ^ c);
            if (c == 0) {
                if ((a_arr[i] == 0) && (b_arr[i] == 0)) {
                    Dmax = Math.max(Dmax, D);
                    D = 0;
                } else if ((a_arr[i] ^ b_arr[i]) == 1) {
                    D = 2;
                    Dmax = Math.max(Dmax, D);
                    D = 0;
                } else if ((a_arr[i] & b_arr[i]) == 1) {
                    D = 2;
                    Dmax = Math.max(Dmax, D);
                }
            }
            if (c == 1) {
                if ((a_arr[i] == 0) && (b_arr[i] == 0)) {
                    D = D + 2;
                    Dmax = Math.max(Dmax, D);
                    D = 0; // carry dies out
                } else if ((a_arr[i] ^ b_arr[i]) == 1) {
                    D = D + 2;
                    Dmax = Math.max(Dmax, D);
                } else if ((a_arr[i] & b_arr[i]) == 1) {
                    D = D + 2;
                    Dmax = Math.max(Dmax, D);
                    D = 2;
                }
            }
            c = ((a_arr[i] & b_arr[i]) | c & (a_arr[i] ^ b_arr[i]));

        }
        rca.delayOfAdder = Dmax;
        rca.sum = sum;
        rca.carryOut = c;
    }

    // public void execAdder() {
    //     convertIntToBool();
    //     calculateRippleCarryAdder(a, b);
    // }
}

class IntToBin {
    public int[] intToBoolArray(long e) {
        int[] arr = new int[32];
        for (int i = 0; i < 32; i++) {
            arr[i] = (int)(e % 2);
            e = e / 2;
        }
        return arr;
    }
}

class BoolToInteger {
    public long boolArrayToInt(int[] s, int c_32) {
        long decimal = 0;
        for (int i = 0; i < 32; i++) {
            decimal += s[i] * Math.pow(2, i);
        }
        decimal += c_32 * Math.pow(2, 32);
        return decimal;
    }
}

class PrintArray {
    public String displayArray(int[] num) {
        String str = "";
        for (int i = 31; i >= 0; i--) {
            //System.out.print(num[i] + " ");
            str = str + num[i] + " ";
        }
        //System.out.println();
        return str;
    }
}

class RippleCarryAdder_32 {
    public int delayOfAdder;
    public int[] sum;
    public int carryOut;

    public RippleCarryAdder_32() {
    }
}
