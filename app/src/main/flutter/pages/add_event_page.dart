import 'package:flutter/material.dart';
import 'package:flutter_events/model/user_model.dart';

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

class AddEventPage extends StatefulWidget {
  final UserModel userModel;

  const AddEventPage({Key key, this.userModel}) : super(key: key);

  @override
  _AddEventPageState createState() => _AddEventPageState();
}

class _AddEventPageState extends State<AddEventPage> {
  final TextEditingController _titleController = new TextEditingController();
  final TextEditingController _typeController = new TextEditingController();
  final TextEditingController _addressController = new TextEditingController();
  final TextEditingController _districtController = new TextEditingController();
  final TextEditingController _descriptionController =
      new TextEditingController();

  String title;
  String type;
  String description;

  String startTime;
  String endTime;
  String day;
  String month;

  double lat;
  double lon;

  String address;
  String districtName;

  var _pickedTime;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _body(context),
    );
  }

  _body(BuildContext context) {
    return Container(
      child: Stack(
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              UnconstrainedBox(
                child: Placeholder(
                  fallbackHeight: 80,
                  fallbackWidth: 300,
                  color: Colors.transparent,
                ),
              ),
              Container(
                height: 50,
                width: 300,
                child: _titleTextField(),
              ),
              Container(
                height: 50,
                width: 100,
                child: _typeTextField(),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  _startTimeButton(),
                  Text(
                    '--',
                    style: TextStyle(fontSize: 20),
                  ),
                  _endTimeButton(),
                  _dateButton()
                ],
              ),
              Container(
                height: 50,
                width: 300,
                child: _addressTextField(),
              ),
              Container(
                height: 50,
                width: 300,
                child: _districtTextField(),
              ),
              Container(
                height: 50,
                width: 300,
                child: _descriptionTextField(),
              )
            ],
          ),
          Positioned(
            top: 500,
            left: 165,
            child: RaisedButton(
              onPressed: _postEvent(),
              color: Colors.red,
              child: Text(
                'Post Event!',
                style: TextStyle(color: Colors.white),
              ),
            ),
          )
        ],
      ),
    );
  }

  _titleTextField() {
    return TextField(
      controller: _titleController,
      textAlign: TextAlign.center,
      style: TextStyle(fontSize: 40),
      decoration: InputDecoration(
        hintText: 'Title',
      ),
    );
  }

  _typeTextField() {
    return TextField(
      controller: _typeController,
      textAlign: TextAlign.center,
      style: TextStyle(fontSize: 16),
      decoration: InputDecoration(
        hintText: 'Type',
      ),
    );
  }

  _addressTextField() {
    return TextField(
      controller: _addressController,
      textAlign: TextAlign.start,
      style: TextStyle(fontSize: 16),
      decoration: InputDecoration(
        hintText: 'Address: 203 Collins Street',
      ),
    );
  }

  _districtTextField() {
    return TextField(
      controller: _districtController,
      textAlign: TextAlign.start,
      style: TextStyle(fontSize: 16),
      decoration: InputDecoration(
        hintText: 'District: Melbourne VIC',
      ),
    );
  }

  _descriptionTextField() {
    return TextField(
      controller: _descriptionController,
      textAlign: TextAlign.start,
      style: TextStyle(fontSize: 16),
      decoration: InputDecoration(
        hintText: 'Description: ...',
      ),
    );
  }

  _startTimeButton() {
    return RaisedButton(
      color: Colors.transparent,
      elevation: 0.0,
      child: Text(
        this.startTime == null ? 'Start Time' : '${this.startTime}',
        style: TextStyle(fontSize: 18),
      ),
      onPressed: () => _showTimePicker('start'),
    );
  }

  _endTimeButton() {
    return RaisedButton(
      color: Colors.transparent,
      elevation: 0.0,
      child: Text(
        this.endTime == null ? 'End Time' : '${this.endTime}',
        style: TextStyle(fontSize: 18),
      ),
      onPressed: () => _showTimePicker('end'),
    );
  }

  _dateButton() {
    return RaisedButton(
      color: Colors.transparent,
      elevation: 0.0,
      child: Text(
        (this.day == null || this.month == null)
            ? 'Date'
            : '${this.day}  ${MONTH[this.month]}',
        style: TextStyle(fontSize: 18),
      ),
      onPressed: () => _showDatePicker(),
    );
  }

  _showDatePicker() async {
    Locale myLocale = Localizations.localeOf(context);
    var picker = await showDatePicker(
        context: context,
        initialDate: DateTime.now(),
        firstDate: DateTime(2019),
        lastDate: DateTime(2021),
        locale: myLocale);
    setState(() {
      _pickedTime = picker;
      try {
        this.day = _pickedTime.day.toString();
        this.month = _pickedTime.month.toString();
      } catch (e) {}
    });
  }

  _showTimePicker(String name) async {
    MaterialLocalizations _localizations = MaterialLocalizations.of(context);
    var picker =
        await showTimePicker(context: context, initialTime: TimeOfDay.now());
    setState(() {
      try {
        _pickedTime = picker;
        if (name.contains('start')) {
          this.startTime = _localizations.formatTimeOfDay(_pickedTime,
              alwaysUse24HourFormat: true);
        } else if (name.contains('end')) {
          this.endTime = _localizations.formatTimeOfDay(_pickedTime,
              alwaysUse24HourFormat: true);
        }
        print('start: $startTime');
        print('end: $endTime');
      } catch (e) {}
    });
  }

  _postEvent() {}
}
