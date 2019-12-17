//// May 24, 2019
////
//// bluetooth low energy test using flutter_blue plugin - https://github.com/pauldemarco/flutter_blue
////
//// youtube link ----> https://www.youtube.com/watch?v=IdcHW38Pz9s
////
//// device tested: nrf52840from adafruit.com running arduino ble uart example
////                connect the device to your computer serial port
////                press connect button, after it connect it will go to another screen
////                press send data to device button and you should see 'LED.set()\n'
////
//// device tested: Puck.js from espruino.com
////                change device name to your puck name (use nrf connect or bluefruit app to find name)
////                press connect button, after it connect it will go to another screen
////                press send data to device button and the red led should turn on
////
//// lower to flutter v1.3.0 if it crashing about UIThread
//// bluetooth device and bluetooth uart transmit characteristic is passed to second screen
//// 2nd to the last statement is the actual sending of data
//// you can change device name, service, and characteristic to match your device
////
//// this is a flutter tutorial for me :-)
////
//
//import 'dart:async';
//import 'dart:convert'; // needed by utf8.encode
//import 'package:flutter/material.dart';
//import 'package:flutter_blue/flutter_blue.dart'; // https://github.com/pauldemarco/flutter_blue
//
//void main() => runApp(MyApp());
//
//class MyApp extends StatelessWidget {
//  // This widget is the root of your application.
//  @override
//  Widget build(BuildContext context) {
//    return MaterialApp(
//      home: Scaffold(
//        appBar: AppBar(
//          title: Text("Bluetooth LE Test"),
//        ),
//        body: FirstScreen(),
//      ),
//    );
//  }
//}
//
//// widget class
//class FirstScreen extends StatefulWidget {
//  @override
//  FirstScreenState createState() => FirstScreenState();
//}
//
//// state class
//// We will replace this class in each of the examples below
//class FirstScreenState extends State<FirstScreen> {
//  static final inputDevice = TextEditingController();
//  static final inputService = TextEditingController();
//  static final inputCharacteristic = TextEditingController();
//
//  String _textString =
//      'ble test for adafruit.com nrf52840 or espruino.com puck.js';
//  String textSource = 'source at http://sayaw.net/main_dart.txt';
//  String buttonLabel = 'connect';
//
//  FlutterBlue fb = FlutterBlue.instance;
//  BluetoothDevice bleDev;
//  StreamSubscription scanSub;
//  StreamSubscription devConnect;
//  BluetoothCharacteristic tx = null;
//
//  bool showProgressIndicator = false;
//
//  @override
//  void initState() {
//    super.initState();
//    inputDevice.text = 'Bluefruit52';
//    inputService.text = '6E400001-B5A3-F393-E0A9-E50E24DCCA9E';
//    inputCharacteristic.text = '6E400002-B5A3-F393-E0A9-E50E24DCCA9E';
//  }
//
//  // The State class must include this method, which builds the widget
//  @override
//  Widget build(BuildContext context) {
//    return Column(
//      children: [
//        new Container(
//          child: showProgressIndicator ? CircularProgressIndicator() : Text(""),
//        ),
//        Text(
//          _textString,
//          style: TextStyle(fontSize: 15),
//        ),
//        TextField(
//          controller: inputDevice,
//          decoration: InputDecoration(
//            labelText: 'device name',
//          ),
//        ),
//        TextField(
//          controller: inputService,
//          decoration: InputDecoration(
//            labelText: 'service uuid', // nordic uart service
//          ),
//        ),
//        TextField(
//          controller: inputCharacteristic,
//          decoration: InputDecoration(
//            labelText: 'characteristic uuid', // nordic uart tx characteristic
//          ),
//        ),
//        RaisedButton(
//          //                         <--- Button
//          child: Text(buttonLabel),
//          onPressed: () {
//            setState(() {
//              showProgressIndicator = true;
//              buttonLabel = 'connected';
//            });
//            getConnected();
//          },
//        ),
//        Text(
//          textSource,
//          style: TextStyle(fontSize: 12),
//        ),
//      ],
//    );
//  }
//
//  // this private method is run whenever the button is pressed
//  bool getConnected() {
//    scanSub = fb.scan().listen((res) {
//      if (res.advertisementData.localName == inputDevice.text) {
//        // match device name
//        scanSub?.cancel();
//        scanSub = null;
//        devConnect = fb.connect(res.device).listen((connect) {
//          // connect device
//          if (connect == BluetoothDeviceState.connected) {
//            bleDev = res.device;
//            bleDev.discoverServices().then((services) {
//              // discover services
//              services.forEach((service) {
//                if (service.uuid.toString().toLowerCase() ==
//                    inputService.text.toLowerCase()) {
//                  service.characteristics.forEach((ch) {
//                    if (ch.uuid.toString().toLowerCase() ==
//                        inputCharacteristic.text.toLowerCase()) {
//                      tx = ch;
//                      showProgressIndicator = false;
//                      Navigator.push(
//                          context,
//                          MaterialPageRoute(
//                              builder: (context) => SecondScreen(
//                                  bleDev: bleDev, tx: tx))); // navigator push
//                    }
//                    ; // if char match
//                  }); //for each characteristic
//                } // service match
//              }); //for each service
//            }); // discover services
//          } // connected
//        }); // try to connect
//      } // if device match
//    }); //scan
//  } // getConnected
//} //_FirstScreenState
//
//class SecondScreen extends StatelessWidget {
//  final BluetoothDevice bleDev;
//  final BluetoothCharacteristic tx;
//
//  SecondScreen({Key key, @required this.bleDev, this.tx}) : super(key: key);
//
//  @override
//  Widget build(BuildContext context) {
//    return Scaffold(
//      appBar: AppBar(title: Text('Second screen')),
//      body: Column(
//        children: [
//          RaisedButton(
//            child: Text(
//              'send data to device',
//              style: TextStyle(fontSize: 24),
//            ),
//            onPressed: () {
//              sendData();
//            },
//          ),
//          RaisedButton(
//            child: Text(
//              'Go back to first screen',
//              style: TextStyle(fontSize: 24),
//            ),
//            onPressed: () {
//              _goBackToFirstScreen(context);
//            },
//          ),
//        ],
//      ),
//    );
//  }
//
//  void _goBackToFirstScreen(BuildContext context) {
//    Navigator.pop(context);
//  }
//
//  void sendData() async {
//    List<int> bytes = utf8.encode('LED.set()\n');
//    final value = await bleDev.writeCharacteristic(tx, bytes,
//        type: CharacteristicWriteType.withoutResponse);
//  }
//}
