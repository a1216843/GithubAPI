package com.example.githubapi.data

// API 통신 결과에 대한 처리를 BaseResponse를 사용해서 처리한다.
// View는 통신 결과의 성공, 실패, 에러 등만 확인할 수 있고 데이터의 출처(local or remote)가 어디인지 등의 정보는 알 수 없다.
interface BaseResponse<T> {
    fun onSuccess(data : T)
    fun onFail(description: String)
    fun onError(throwable: Throwable)
    fun onLoading()
    fun onLoaded()
}