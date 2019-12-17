import 'dart:async';
import 'dart:convert';
import 'package:flutter_events/model/user_operation_model.dart';
import 'package:http/http.dart' as http;

class UserOperationDao {
  static Future<UserOperationModel> fetch(String url) async {
    final response = await http.get(url);
    if (response.statusCode == 200) {
      Utf8Decoder utf8decoder = Utf8Decoder();
      var result = json.decode(utf8decoder.convert(response.bodyBytes));
      UserOperationModel model = UserOperationModel.fromJson(result);
      return model;
    } else {
      throw Exception('Failed to load return data.');
    }
  }
}
