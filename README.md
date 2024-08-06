backend : pom.xml을 새로 생성한 프로젝트의 파일로 변경해주세요
frontend : package.json을 새로 생성한 프로젝트의 파일로 변경해주세요

임시로 폴더구조를 만들어 둔 것이므로, .temp는 삭제하고 폴더 구조도 변경하셔도 됩니다.

Todo :

1. 8/4 ~ : 기본적으로 접속 가능한 리액트 프로젝트 생성

2. 8/4 ~ : 기본적으로 접속 가능한 스프링 프로젝트 생성

3. 8/4 ~ : 생성된 프로젝트를 각각 도커 이미지화

4. 8/4 ~ : 이미지화 한 각 이미지들을 nginx를 추가한 뒤, docker-compose를 이용하여 내부망 접속 확인

5. 8/4 ~ : 해당 과정 자동화


## rests


```
docker buildx build -t [image_name] [dockerfile_directory]
```
options : --momory=4g --network host
(안되면 네트워크 옵션 제거)