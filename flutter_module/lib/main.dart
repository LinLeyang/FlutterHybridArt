import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';

void main() {
  FlutterBoost.singleton.registerPageBuilders({
    'penta://firstPage': (pageName, params, _) {
      print("first flutterPage params:$params");
      return MyHomePage();
    },
    'penta://secondPage': (pageName, params, _) {
      print("second flutterPage params:$params");
      return MySecondPage();
    },
  });

  return runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      //initialRoute: "/",
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      builder: FlutterBoost.init(),
      home: Container(),
//      routes: {
//        "/": (context) => MyHomePage(title: 'Flutter Demo Home Page'),
//        "/second": (context) => MySecondPage(),
//      },
    );
  }
}

class MySecondPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("MySecondPage"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              '这是MySecondPage',
            ),
          ],
        ),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              '这是MyHomePage',
            ),
          ],
        ),
      ),
      floatingActionButton: FlatButton(
        child: Text("点击"),
        onPressed: () {
          MyMessageChannel.foo();
        },
      ),
    );
  }
}

class MyMessageChannel {
  static const MethodChannel methodChannel =
      MethodChannel('penta.com:FlutterHybridArt');

  static Future<String> foo() async {
    String data;
    try {
      final result =
          await methodChannel.invokeMethod('foo', {"msg": "hahahah"});
      data = result;
      debugPrint("MethodChannel:" + result);
    } on PlatformException {
      data = null;
    }
    return data;
  }
}
