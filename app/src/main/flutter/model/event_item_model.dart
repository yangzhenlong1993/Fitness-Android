import 'package:flutter_events/model/user_model.dart';

class EventItemModel {
  final int eventId;
  final String title; //Display name
  final String type; //Running
  final String description;

  final String startTime;
  final String endTime;
  final String day;
  final String month;

  final double lat;
  final double lon;
  final String address; //152 La Trobe Street
  final String districtName; //Melbourne VIC

  final String url;
  final List<UserModel> participants;

  EventItemModel(
      {this.eventId,
      this.title,
      this.type,
      this.description,
      this.startTime,
      this.endTime,
      this.day,
      this.month,
      this.lat,
      this.lon,
      this.address,
      this.districtName,
      this.url,
      this.participants});

  factory EventItemModel.fromJson(Map<String, dynamic> json) {

    var participantsJson = json['participants'] as List;
    List<UserModel> participants = [];
    if (participantsJson != null) {
      participants =
          participantsJson.map((i) => UserModel.fromJson(i)).toList();
    }

    return EventItemModel(
      eventId: json['eventId'],
      title: json['title'],
      type: json['type'],
      description: json['description'],
      startTime: json['startTime'],
      endTime: json['endTime'],
      day: json['day'],
      month: json['month'],
      lat: json['lat'],
      lon: json['lon'],
      address: json['address'],
      districtName: json['districtName'],
      url: json['url'],
      participants: participants,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['eventId'] = this.eventId;
    data['title'] = this.title;
    data['type'] = this.type;
    data['description'] = this.description;

    data['startTime'] = this.startTime;
    data['endTime'] = this.endTime;
    data['day'] = this.day;
    data['month'] = this.month;

    data['lat'] = this.lat;
    data['lon'] = this.lon;
    data['address'] = this.address;
    data['districtName'] = this.districtName;

    data['url'] = this.url;
    data['participants']=this.participants;

    return data;
  }
}
