package com.wiio.androidwiiovision

data class ConfigData( var transferSettingData: TransferSettingData, var languageSetting:LanguageSettingData, var imageSettingData: ImageSettingData)

data class TransferSettingData(var user:String, var password:String, var root:String, var ip:String, var mode:String)

data class LanguageSettingData(var language:String)

data class ImageSettingData(var quality:String)