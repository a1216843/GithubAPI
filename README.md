# Repository Pattern
## Data Source
local과 remote로 구분됨, 데이터를 가져오는 곳
## Model
RepoModel, UserModel 등 Data Source에서 가져온 데이터 형식
## Item
RepoItem, UserItem 등 Data Source에서 가져온 데이터를 Application(UI layer)에서 사용하는 형식으로 변경한 가공된 데이터
## Repository
Application과 Data Source 사이의 중개 레이어, Application의 요청에 의한 데이터 호출 및 Application에 데이터 혹은 에러 등의 호출 결과를 보내줌
## Repository Pattern의 적용
View는 오로지 가공된 데이터를 받아 처리해야 하며 통신과 관련된 그 어떤 것도 알지 못하도록 구현하기 위해 기존의 SearchFragment에 있던 repoAPI 등의 데이터 호출
부분을 Repository 패턴을 적용하여 View에서 분리시켰음
