import 'package:flutter_events/model/user_model.dart';

class AccountModel {

  final int errno;
  final UserModel data;

  AccountModel({this.errno, this.data});

  factory AccountModel.fromJson(Map<String, dynamic> json) {
    var dataJson = json['data'];
    UserModel userModel;

    if (dataJson != null) {
      userModel = UserModel.fromJson(json['data']);
    }

    return AccountModel(errno: json['errno'], data: userModel);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['errno'] = this.errno;
    data['data'] = this.data;

    return data;
  }
}
