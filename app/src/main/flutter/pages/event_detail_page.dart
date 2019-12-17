import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_events/dao/user_operation_dao.dart';
import 'package:flutter_events/model/event_item_model.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/model/user_operation_model.dart';
import 'package:flutter_events/pages/prompt_page.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

const MONTH = {
  '01': 'Jan',
  '02': 'Feb',
  '03': 'Mar',
  '04': 'Apr',
  '05': 'May',
  '06': 'Jun',
  '07': 'Jul',
  '08': 'Aug',
  '09': 'Sep',
  '10': 'Oct',
  '11': 'Nov',
  '12': 'Dec'
};

class EventDetailPage extends StatefulWidget {

  final UserModel userModel;
  final EventItemModel item;

  const EventDetailPage({Key key, this.item, this.userModel}) : super(key: key);

  @override
  _EventDetailPageState createState() => _EventDetailPageState();
}

class _EventDetailPageState extends State<EventDetailPage> {
  String status;
  double distance;
  var _onPressed;
  PromptPage promptPage = new PromptPage();

  @override
  void initState() {
    super.initState();
    this.status = _setStatus(widget.item);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xfff2f2f2),
      body: _body(context),
    );
  }

  _body(BuildContext context) {
    return Stack(
      children: <Widget>[
        //map
        Container(
          padding: EdgeInsets.only(top: 63),
          height: 350,
          width: 450,
          child: _googleMap(),
        ),
        //TODO: display event detail
        ListView(
          padding: EdgeInsets.fromLTRB(15, 0, 15, 20),
          children: <Widget>[
            UnconstrainedBox(
              child: Placeholder(
                fallbackHeight: 350,
                fallbackWidth: 380,
                color: Colors.transparent,
              ),
            ),
            //details
            Container(
              width: 450,
              height: 500,
              decoration: BoxDecoration(
                  color: Colors.white, borderRadius: BorderRadius.circular(7)),
              child: _info(),
            ),
          ],
        ),
        _appBar(Colors.red, Colors.white),
        Positioned(
          top: 600,
          left: 160,
          child: _joinButton(widget.item, status, widget.userModel),
        )
      ],
    );
  }

  _appBar(Color backgroundColor, Color backButtonColor) {
    return Container(
      height: 70,
      color: backgroundColor,
      child: FractionallySizedBox(
        widthFactor: 1,
        child: Stack(
          children: <Widget>[
            //Return button
            GestureDetector(
              onTap: () {
                Navigator.pop(context);
              },
              child: Container(
                margin: EdgeInsets.fromLTRB(8, 35, 10, 5),
                child: Icon(
                  Icons.close,
                  color: backButtonColor,
                  size: 26,
                ),
              ),
            ),
            // Title
            Positioned(
              left: 0,
              right: 0,
              top: 35,
              child: Center(
                child: Text(
                  'Detail',
                  style: TextStyle(color: backButtonColor, fontSize: 22),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  _googleMap() {
    Completer<GoogleMapController> _controller = Completer();

    LatLng targetLatLng = LatLng(widget.item.lat, widget.item.lon);
    CameraPosition _position = CameraPosition(
      target: targetLatLng,
      zoom: 13.7,
    );
    List<Marker> markers = <Marker>[];
    markers.add(Marker(
        markerId: MarkerId(widget.item.title),
        position: targetLatLng,
        infoWindow: InfoWindow(title: widget.item.title)));

    return GoogleMap(
      initialCameraPosition: _position,
      mapType: MapType.normal,
      markers: Set<Marker>.of(markers),
      onMapCreated: (GoogleMapController controller) {
        _controller.complete(controller);
      },
    );
  }

  _info() {
    return Container(
      padding: EdgeInsets.fromLTRB(10, 23, 15, 10),
      child: Column(
        children: <Widget>[
          //title
          _title(),

          //location + distance
          _location(),

          //time
          _time(),

          //description
          _description(),

          //TODO: participants
          _participants(widget.item),
        ],
      ),
    );
  }

  _title() {
    return Container(
//          color: Colors.grey,
      padding: EdgeInsets.only(bottom: 15),
      child: Text(
        widget.item.title,
        textAlign: TextAlign.center,
        style: TextStyle(color: Colors.black, fontSize: 40),
      ),
    );
  }

  _location() {
    return Container(
      padding: EdgeInsets.fromLTRB(15, 0, 0, 15),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Icon(
            Icons.location_on,
            size: 18,
          ),
          Container(
            padding: EdgeInsets.only(left: 2),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Container(
                  width: 300,
                  child: Text(
                    '${widget.item.address}, ${widget.item.districtName}',
                    softWrap: true,
                    textAlign: TextAlign.left,
                    style: TextStyle(fontSize: 16),
                  ),
                ),
                FutureBuilder<Widget>(
                    future: _distanceText(widget.item),
                    builder:
                        (BuildContext context, AsyncSnapshot<Widget> snapshot) {
                      if (snapshot.hasData) return snapshot.data;
                      return Text(
                        'loading',
                        style: TextStyle(color: Colors.black45, fontSize: 14),
                      );
                    }),
              ],
            ),
          )
        ],
      ),
    );
  }

  _time() {
    return Container(
      padding: EdgeInsets.fromLTRB(15, 0, 0, 15),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Icon(
            Icons.access_time,
            size: 18,
          ),
          Container(
            padding: EdgeInsets.only(left: 2),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Container(
                  child: Text(
                    '${widget.item.startTime} - ${widget.item.endTime}  ${widget.item.day} ${MONTH[widget.item.month]}',
                    softWrap: true,
                    textAlign: TextAlign.left,
                    style: TextStyle(fontSize: 16),
                  ),
                ),
                Container(
                  padding: EdgeInsets.only(top: 3),
                  child: _statusText(status),
                )
              ],
            ),
          )
        ],
      ),
    );
  }

  _description() {
    return Container(
      decoration: BoxDecoration(
          border: Border(top: BorderSide(width: 0.3, color: Colors.grey))),
      padding: EdgeInsets.fromLTRB(15, 15, 13, 15),
      alignment: Alignment.topLeft,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Container(
            padding: EdgeInsets.only(bottom: 10),
            child: Text(
              'ABOUT',
              textAlign: TextAlign.left,
              style: TextStyle(fontSize: 20),
            ),
          ),
          Text(
            widget.item.description,
            textAlign: TextAlign.left,
            softWrap: true,
            style: TextStyle(fontSize: 16),
          )
        ],
      ),
    );
  }

  _participants(EventItemModel item) {
    List<String> participantNames = [];
    if (item.participants.length != 0) {
      participantNames = item.participants.map((user) => user.name).toList();
      print(participantNames);
    }

    List<Widget> nameWidgets = [];
    participantNames.forEach((name) => nameWidgets.add(Text(
          '$name,  ',
          style: TextStyle(fontSize: 16),
        )));

    return Container(
      decoration: BoxDecoration(
          border: Border(top: BorderSide(width: 0.3, color: Colors.grey))),
      padding: EdgeInsets.fromLTRB(15, 15, 13, 50),
      alignment: Alignment.topLeft,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Container(
            padding: EdgeInsets.only(bottom: 10),
            child: Text(
              'PARTICIPANTS',
              textAlign: TextAlign.left,
              style: TextStyle(fontSize: 20),
            ),
          ),
          //TODO: list of participants, user names should be fine...
          Container(
            child: Row(
              children: nameWidgets,
            ),
          )
        ],
      ),
    );
  }

  _setStatus(EventItemModel item) {
    if (item == null) return '';
    var dateTime = new DateTime.now();
    var startDateTime =
        DateTime.parse('2019-${item.month}-${item.day} ${item.startTime}:00');
    var endDateTime =
        DateTime.parse('2019-${item.month}-${item.day} ${item.endTime}:00');

    if (dateTime.isBefore(startDateTime)) {
      return 'Upcoming';
    } else if (dateTime.isAfter(endDateTime)) {
      return 'Past';
    } else {
      return 'Now';
    }
  }

  _statusText(String status) {
    if (status.contains('Upcoming')) {
      return Text(
        '$status',
        style: TextStyle(fontSize: 15, color: Colors.blueAccent),
      );
    } else if (status.contains('Now')) {
      return Text(
        '$status',
        style: TextStyle(fontSize: 15, color: Colors.red),
      );
    } else if (status.contains('Past')) {
      return Text(
        '$status',
        style: TextStyle(fontSize: 15, color: Colors.grey),
      );
    }
    return '';
  }

  Future<Widget> _distanceText(EventItemModel item) async {
    if (item == null) return null;
    double distance;
    String result;
    Position current = await Geolocator()
        .getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    Position target = new Position(latitude: item.lat, longitude: item.lon);

    distance = await Geolocator().distanceBetween(
        current.latitude, current.longitude, target.latitude, target.longitude);

    if (distance < 100) {
      result = '< 100 m';
    } else if (distance >= 100 && distance < 1000) {
      result = distance.round().toString() + ' m';
    } else {
      result = (distance / 1000).toStringAsFixed(1) + ' km';
    }

    return Container(
      padding: EdgeInsets.only(top: 3),
      child: Text(
        result,
        style: TextStyle(color: Colors.black45, fontSize: 15),
        textAlign: TextAlign.left,
      ),
    );
  }

  _joinButton(EventItemModel item, String status, UserModel user) {
    String text = 'JOIN!';
    Color buttonColor = Colors.red;
    List<int> joinedEventIds = [];
    if (user.joinedEvents.length != 0) {
      joinedEventIds = user.joinedEvents.map((event) => event.eventId).toList();
      print(joinedEventIds);
    }

    if (status.contains('Past')) {
      _onPressed = null;
      text = 'EXPIRED';
      buttonColor = Colors.grey;
    } else if (joinedEventIds.contains(item.eventId)) {
      _onPressed = null;
      text = 'JOINED';
      buttonColor = Colors.grey;
    } else {
      text = 'JOIN NOW!';
      _onPressed = () {
        String url =
            'http://10.0.2.2:5000/join?userId=${user.userId}&eventId=${item.eventId}';
        UserOperationDao.fetch(url).then((UserOperationModel model) async {
          print(model.errno);
          if (model.errno == 0) {
            await promptPage.showMessage(context, "Event joined!");
            setState(() {
              user.joinedEvents.add(item);
              item.participants.add(user);
            });
          }
        }).catchError((e) {});
      };
    }

    return RaisedButton(
      onPressed: _onPressed,
      child: Text(
        text,
        style: TextStyle(color: Colors.white),
      ),
      color: buttonColor,
    );
  }
}
