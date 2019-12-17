import 'dart:async';
import 'dart:convert';
import 'package:flutter_events/model/search_model.dart';
import 'package:http/http.dart' as http;

const URL = 'http://10.0.2.2:5000/';

class SearchDao {
  static Future<SearchModel> fetch(String args, String text) async {
    String url = URL + args;
    final response = await http.get(url);
    if (response.statusCode == 200) {
      Utf8Decoder utf8decoder = Utf8Decoder();
      var result = json.decode(utf8decoder.convert(response.bodyBytes));
      //render only when input = server response
      SearchModel model = SearchModel.fromJson(result);
      model.keyword = text;
      return model;
    } else {
      throw Exception('Failed to load event data.');
    }
  }
}
