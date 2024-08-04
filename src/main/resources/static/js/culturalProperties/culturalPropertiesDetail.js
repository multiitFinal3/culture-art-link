//$(document).ready(function() {
//
////    // URL에서 문화재 ID 가져오기
////    const pathSegments = window.location.pathname.split('/');
////    const id = pathSegments[pathSegments.length - 1]; // 마지막 세그먼트가 ID
//////    const culturalPropertiesId = pathSegments[pathSegments.length - 1]; // 마지막 세그먼트가 ID
////console.log('id:'+ id);
//////console.log("문화재 ID:", culturalPropertiesId);
//////console.log('culturalPropertiesId:'+ culturalPropertiesId);
////
//////fetchCulturalPropertyDetail(id);
////
//////getCulturalPropertyDetail(1);
//////
//////
////function getCulturalPropertyDetail() {
////    $.ajax({
////        url: `/cultural-properties/detail/${id}`,
////        method: 'GET',
//////        dataType: 'json',
////        success: function(property) {
////
////        console.log('id22:'+ id);
////        console.log("문화재 정보:", property);
////
////            // 데이터가 정상적으로 로드되면 HTML에 추가
////            $('#culturalPropertiesName').text(property.culturalPropertiesName);
////            $('#mainImgUrl').attr('src', property.mainImgUrl);
////            $('#content').text(property.content);
////            // 추가적인 요소도 필요에 따라 설정
////            console.log('사용된 ID:', id);
////        },
////        error: function(xhr, status, error) {
////            console.error('상세 정보 로드 실패:', status, error);
////        }
////    });
////}
//
//
//getCulturalPropertyDetail();
//
//    // 문화재 상세 정보를 가져오는 함수 정의
//    function getCulturalPropertyDetail(id) {
////        // URL에서 문화재 ID 가져오기
////        const pathSegments = window.location.pathname.split('/');
////        const id = pathSegments[pathSegments.length - 1];
//
//        // AJAX를 통해 서버로부터 문화재 상세 정보를 요청
//        $.ajax({
//            url: `/cultural-properties/detail/${id}`,
//            method: 'GET',
//            dataType: 'json',
//            success: function(property) {
//                console.log('문화재 정보:', property);
//
//                // 가져온 정보를 화면에 표시
//                $('#culturalPropertiesName').text(property.culturalPropertiesName);
//                $('#mainImgUrl').attr('src', property.mainImgUrl);
//                $('#content').text(property.content);
//                // 추가적인 정보도 필요에 따라 설정
//
//                console.log('사용된 ID:', id);
//            },
//            error: function(xhr, status, error) {
//                console.error('상세 정보 로드 실패:', status, error);
//            }
//        });
//    }
//
////console.log("문화재 정보:", property);
////            // 성공적으로 데이터를 받아온 경우
////            $('#culturalPropertiesName').text(property.culturalPropertiesName);
////            $('#content').text(property.content);
////            $('#address').text(property.region + ' ' + property.district);
////            $('#dynasty').text(property.dynasty || '시대 정보 없음');
////            $('#imgUrl').text(property.imgUrl && property.imgUrl.length > 0 ? '이미지 있음' : '이미지 없음');
////            $('#videoUrl').text(property.videoUrl && property.videoUrl.length > 0 ? '동영상 있음' : '동영상 없음');
////            $('#narrationUrl').text(property.narrationUrl && property.narrationUrl.length > 0 ? '나레이션 있음' : '나레이션 없음');
////
////            // 이미지 및 버튼의 URL 설정
////            $('#mainImgUrl').attr('src', property.mainImgUrl);
////            $('#imageButton').attr('onclick', `showImage('${property.imgUrl ? property.imgUrl[0] : ''}')`);
////            $('#videoButton').attr('onclick', `showVideo('${property.videoUrl && property.videoUrl.length > 0 ? property.videoUrl[0] : ''}')`);
////            $('#narrationButton').attr('onclick', `showNarration('${property.narrationUrl && property.narrationUrl.length > 0 ? property.narrationUrl[0] : ''}')`);
////        },
////        error: function() {
////            alert('문화재 정보를 가져오는 데 실패했습니다.');
////        }
//
//
//
//
////function getCulturalPropertyDetail(id) {
////    $.ajax({
////        url: `/cultural-properties/detail/${id}`,
////        method: 'GET',
////        success: function(property) {
////            // 데이터가 정상적으로 로드되면 HTML에 추가
////            $('#propertyName').text(property.culturalPropertiesName);
////            $('#propertyImage').attr('src', property.mainImgUrl);
////            $('#propertyDescription').text(property.description);
////            // 추가적인 요소도 필요에 따라 설정
////        },
////        error: function(xhr, status, error) {
////            console.error('상세 정보 로드 실패:', status, error);
////        }
////    });
////}
//
////// 페이지 로드 시 특정 ID의 문화재 상세 정보를 가져옵니다.
////$(document).ready(function() {
////    const id = /* 페이지에서 ID를 가져오는 코드 */;
////    fetchCulturalPropertyDetail(id);
////});
//
//
//
//
////
////function fetchCulturalPropertyDetail(id) {
////        $.ajax({
////            url: `/cultural-properties/detail/${id}`,
////            method: 'GET',
////            success: function(property) {
////                $('#culturalPropertiesName').text(property.culturalPropertiesName);
////                $('#content').text(property.content);
////                $('#address').text(property.address);
////                $('#dynasty').text(property.dynasty);
////                $('#mainImgUrl').attr('src', property.mainImgUrl);
////            },
////            error: function() {
////                alert('문화재 정보를 불러오는 데 실패했습니다.');
////            }
////        });
////    }
////
////    fetchCulturalPropertyDetail(1);
//
////    // 페이지가 로드될 때 문화재 정보를 가져오도록 호출
////    $(document).ready(function() {
////        // 예를 들어, ID가 1인 문화재 정보 요청
////        fetchCulturalPropertyDetail(1);
////    });
//
//
//
////
////$.ajax({
////            url: `/cultural-properties/detail/${id}`,
////            method: 'GET',
////            success: function(data) {
////                // 데이터 로딩 후 화면 업데이트
////                $('#culturalPropertiesName').text(data.culturalPropertiesName);
////                $('#content').text(data.content);
////                $('#address').text(data.address);
////                $('#dynasty').text(data.dynasty);
////                $('#mainImgUrl').attr('src', data.mainImgUrl);
////
////                // 버튼 URL 업데이트
////                $('#imageButton').attr('onclick', `showImage('${data.imgUrl}')`);
////                $('#videoButton').attr('onclick', `showVideo('${data.videoUrl}')`);
////                $('#narrationButton').attr('onclick', `showNarration('${data.narrationUrl}')`);
////            },
////            error: function() {
////                alert('문화재 정보를 불러오는 데 실패했습니다.');
////            }
////        });
////    });
////
//
////    // getDetail 함수를 호출하여 문화재 정보 가져오기
////    getDetail(culturalPropertiesId);
////
////    function getDetail(id) {
////        $.ajax({
//////            url: `/cultural-properties/detail/${culturalPropertiesId}`, // 수정된 부분
////url: `/cultural-properties/detail/${id}`, // 수정된 부분
//////            console.log("AJAX 요청 URL:", `/cultural-properties/detail/${culturalPropertiesId}`);
////            type: 'GET',
//////            dataType: 'json',
////            contentType: 'application/json',
////            success: function(property) {
////                console.log("문화재 정보:", property);
////                // 성공적으로 데이터를 받아온 경우
////                $('#culturalPropertiesName').text(property.culturalPropertiesName);
////                $('#content').text(property.content);
////                $('#address').text(property.region + ' ' + property.district);
////                $('#dynasty').text(property.dynasty || '시대 정보 없음');
////                $('#imgUrl').text(property.imgUrl && property.imgUrl.length > 0 ? '이미지 있음' : '이미지 없음');
////                $('#videoUrl').text(property.videoUrl && property.videoUrl.length > 0 ? '동영상 있음' : '동영상 없음');
////                $('#narrationUrl').text(property.narrationUrl && property.narrationUrl.length > 0 ? '나레이션 있음' : '나레이션 없음');
////
////                // 이미지 및 버튼의 URL 설정
////                $('#mainImgUrl').attr('src', property.mainImgUrl);
////                $('#imageButton').attr('onclick', `showImage('${property.imgUrl ? property.imgUrl[0] : ''}')`);
////                $('#videoButton').attr('onclick', `showVideo('${property.videoUrl && property.videoUrl.length > 0 ? property.videoUrl[0] : ''}')`);
////                $('#narrationButton').attr('onclick', `showNarration('${property.narrationUrl && property.narrationUrl.length > 0 ? property.narrationUrl[0] : ''}')`);
////            },
////            error: function(xhr, status, error) {
////                console.log("AJAX 오류 상태 코드:", xhr.status);
////                console.log("AJAX 오류 응답 텍스트:", xhr.responseText);
////                alert('문화재 정보를 가져오는 데 실패했습니다.');
////            }
////        });
////    }
////
////    function showImage(imageUrl) {
////        $('#modalImage').attr('src', imageUrl);
////        $('#imageModal').modal('show');
////    }
////
////    function showVideo(videoUrl) {
////        $('#modalVideo').attr('src', videoUrl);
////        $('#videoModal').modal('show');
////    }
////
////    function showNarration(narrationUrl) {
////        $('#modalAudio').find('source').attr('src', narrationUrl);
////        $('#modalAudio')[0].load(); // 새로 로드
////        $('#audioModal').modal('show');
////    }
//
//
////    // AJAX 요청으로 문화재 정보 가져오기
////    $.ajax({
////        url: `/cultural-properties/detail/${culturalPropertiesId}`,
////        type: 'GET',
//////        dataType: 'json',
////        success: function(property) {
////            console.log("문화재 정보:", property);
////            // 성공적으로 데이터를 받아온 경우
////            $('#culturalPropertiesName').text(property.culturalPropertiesName);
////            $('#content').text(property.content);
////            $('#address').text(property.region + ' ' + property.district);
////            $('#dynasty').text(property.dynasty || '시대 정보 없음');
////            $('#imgUrl').text(property.imgUrl && property.imgUrl.length > 0 ? '이미지 있음' : '이미지 없음');
////            $('#videoUrl').text(property.videoUrl && property.videoUrl.length > 0 ? '동영상 있음' : '동영상 없음');
////            $('#narrationUrl').text(property.narrationUrl && property.narrationUrl.length > 0 ? '나레이션 있음' : '나레이션 없음');
////
////            // 이미지 및 버튼의 URL 설정
////            $('#mainImgUrl').attr('src', property.mainImgUrl);
////            $('#imageButton').attr('onclick', `showImage('${property.imgUrl ? property.imgUrl[0] : ''}')`);
////            $('#videoButton').attr('onclick', `showVideo('${property.videoUrl && property.videoUrl.length > 0 ? property.videoUrl[0] : ''}')`);
////            $('#narrationButton').attr('onclick', `showNarration('${property.narrationUrl && property.narrationUrl.length > 0 ? property.narrationUrl[0] : ''}')`);
////        },
////        error: function() {
////            alert('문화재 정보를 가져오는 데 실패했습니다.');
////        }
////    });
////
////
////        function showImage(imageUrl) {
////            $('#modalImage').attr('src', imageUrl);
////            $('#imageModal').modal('show');
////        }
////
////        function showVideo(videoUrl) {
////            $('#modalVideo').attr('src', videoUrl);
////            $('#videoModal').modal('show');
////        }
////
////        function showNarration(narrationUrl) {
////            $('#modalAudio').find('source').attr('src', narrationUrl);
////            $('#modalAudio')[0].load(); // 새로 로드
////            $('#audioModal').modal('show');
////        }
//
////    // URL에서 문화재 ID 가져오기
////        const urlParams = new URLSearchParams(window.location.search);
////        const culturalPropertiesId = urlParams.get('id'); // 'id'라는 파라미터가 있다고 가정
//
////    if (culturalPropertiesId) {
////        // 문화재 상세 정보를 가져오기
////        $.ajax({
////            url: `/cultural-properties/detail/${culturalPropertiesId}`, // 서버 API URL
////            type: 'GET',
////            success: function(data) {
////                // 서버에서 반환된 HTML을 삽입합니다.
////                $('#contentArea').html(data); // contentArea는 데이터를 삽입할 div의 id입니다.
////            },
////            error: function(xhr, status, error) {
////                console.error('문화재 정보 로드 실패:', status, error);
////                alert('문화재 정보를 불러오는 데 실패했습니다.');
////            }
////        });
////    } else {
////        alert('문화재 ID를 찾을 수 없습니다.');
////    }
//
//
//
//
//
////    if (culturalPropertiesId) {
////            // 문화재 상세 정보를 가져오기
////            $.ajax({
////                url: `/cultural-properties/detail/${culturalPropertiesId}`, // 서버 API URL
////                type: 'GET',
////                success: function(data) {
////                    // data는 서버에서 응답으로 받은 HTML
////                    $('#contentArea').html(data); // contentArea는 데이터를 삽입할 div의 id입니다.
////    // data는 서버에서 응답으로 받은 데이터 (JSON 형식이어야 함)
////                    $('#culturalPropertiesName').text(data.culturalPropertiesName);
////                    $('#mainImgUrl').attr('src', data.mainImgUrl);
////                    $('#content').text(data.description);
////                    $('#address').text(data.region + ' ' + data.district);
////                    $('#dynasty').text(data.period || '시대 정보 없음');
////                    $('#imgUrl').text(data.hasImage ? '이미지 있음' : '이미지 없음');
////                    $('#videoUrl').text(data.hasVideo ? '동영상 있음' : '동영상 없음');
////                    $('#narrationUrl').text(data.hasNarration ? '나레이션 있음' : '나레이션 없음');
////
////                    // 버튼 클릭 이벤트 등록
////                    $('#imageButton').click(function() {
////                        if (data.hasImage) {
////                            showImage(data.mainImgUrl);
////                        } else {
////                            alert('이미지가 없습니다.');
////                        }
////                    });
////
////                    $('#videoButton').click(function() {
////                        if (data.hasVideo) {
////                            showVideo(data.videoUrl); // videoUrl 추가 필요
////                        } else {
////                            alert('동영상이 없습니다.');
////                        }
////                    });
////
////                    $('#narrationButton').click(function() {
////                        if (data.hasNarration) {
////                            showNarration(data.narrationUrl); // narrationUrl 추가 필요
////                        } else {
////                            alert('나레이션이 없습니다.');
////                        }
////                    });
////                },
////                error: function(xhr, status, error) {
////                    console.error('문화재 정보 로드 실패:', status, error);
////                    alert('문화재 정보를 불러오는 데 실패했습니다.');
////                }
////            });
////        } else {
////            alert('문화재 ID를 찾을 수 없습니다.');
////        }
//
//
//
////        if (culturalPropertiesId) {
////            $.ajax({
////                url: `/cultural-properties/detail/${culturalPropertiesId}`, // 서버 API URL
//////                url: `/cultural-properties/cultural-properties-detail/${culturalPropertiesId}`, // 서버 API URL
////                type: 'GET',
//////                dataType: 'json',
////                success: function(data) {
//////                    console.log(data);
////                    $('#culturalPropertiesName').text(data.culturalPropertiesName);
////                    $('#mainImgUrl').attr('src', data.mainImgUrl);
////                    $('#content').text(data.description);
////                    $('#address').text(data.region + ' ' + data.district);
////                    $('#dynasty').text(data.period || '시대 정보 없음');
////                    $('#imgUrl').text(data.hasImage ? '이미지 있음' : '이미지 없음');
////                    $('#videoUrl').text(data.hasVideo ? '동영상 있음' : '동영상 없음');
////                    $('#narrationUrl').text(data.hasNarration ? '나레이션 있음' : '나레이션 없음');
////
////                    // 버튼 클릭 이벤트 등록
////                    $('#imageButton').click(function() {
////                        if (data.hasImage) {
////                            showImage(data.mainImgUrl);
////                        } else {
////                            alert('이미지가 없습니다.');
////                        }
////                    });
////
////                    $('#videoButton').click(function() {
////                        if (data.hasVideo) {
////                            showVideo(data.videoUrl); // videoUrl 추가 필요
////                        } else {
////                            alert('동영상이 없습니다.');
////                        }
////                    });
////
////                    $('#narrationButton').click(function() {
////                        if (data.hasNarration) {
////                            showNarration(data.narrationUrl); // narrationUrl 추가 필요
////                        } else {
////                            alert('나레이션이 없습니다.');
////                        }
////                    });
////                },
////                error: function(xhr, status, error) {
////                    console.error('문화재 정보 로드 실패:', status, error);
////                    alert('문화재 정보를 불러오는 데 실패했습니다.');
////                }
////            });
////        } else {
////            alert('문화재 ID를 찾을 수 없습니다.');
////        }
//
//
////    function showImage() {
////        const imgSrc = document.querySelector('img[alt="대표 문화재"]').src; // 대표 이미지 소스 가져오기
////        const imgModal = document.createElement('div');
////        imgModal.className = 'modal fade';
////        imgModal.id = 'imageModal';
////        imgModal.tabIndex = -1;
////        imgModal.role = 'dialog';
////        imgModal.innerHTML = `
////            <div class="modal-dialog" role="document">
////                <div class="modal-content">
////                    <div class="modal-header">
////                        <h5 class="modal-title">이미지 보기</h5>
////                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
////                            <span aria-hidden="true">&times;</span>
////                        </button>
////                    </div>
////                    <div class="modal-body">
////                        <img src="${imgSrc}" class="img-fluid" alt="상세 이미지">
////                    </div>
////                    <div class="modal-footer">
////                        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
////                    </div>
////                </div>
////            </div>
////        `;
////        document.body.appendChild(imgModal);
////        $('#imageModal').modal('show');
////    }
////
////    function showVideo() {
////        const videoSrc = 'YOUR_VIDEO_URL_HERE'; // 동영상 URL
////        const videoModal = document.createElement('div');
////        videoModal.className = 'modal fade';
////        videoModal.id = 'videoModal';
////        videoModal.tabIndex = -1;
////        videoModal.role = 'dialog';
////        videoModal.innerHTML = `
////            <div class="modal-dialog" role="document">
////                <div class="modal-content">
////                    <div class="modal-header">
////                        <h5 class="modal-title">동영상 보기</h5>
////                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
////                            <span aria-hidden="true">&times;</span>
////                        </button>
////                    </div>
////                    <div class="modal-body">
////                        <iframe width="100%" height="315" src="${videoSrc}" frameborder="0" allowfullscreen></iframe>
////                    </div>
////                    <div class="modal-footer">
////                        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
////                    </div>
////                </div>
////            </div>
////        `;
////        document.body.appendChild(videoModal);
////        $('#videoModal').modal('show');
////    }
////
////    function showNarration() {
////        const audioSrc = 'YOUR_AUDIO_URL_HERE'; // 나레이션 URL
////        const audioModal = document.createElement('div');
////        audioModal.className = 'modal fade';
////        audioModal.id = 'audioModal';
////        audioModal.tabIndex = -1;
////        audioModal.role = 'dialog';
////        audioModal.innerHTML = `
////            <div class="modal-dialog" role="document">
////                <div class="modal-content">
////                    <div class="modal-header">
////                        <h5 class="modal-title">나레이션 듣기</h5>
////                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
////                            <span aria-hidden="true">&times;</span>
////                        </button>
////                    </div>
////                    <div class="modal-body">
////                        <audio controls>
////                            <source src="${audioSrc}" type="audio/mpeg">
////                            Your browser does not support the audio element.
////                        </audio>
////                    </div>
////                    <div class="modal-footer">
////                        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
////                    </div>
////                </div>
////            </div>
////        `;
////        document.body.appendChild(audioModal);
////        $('#audioModal').modal('show');
////    }
//
//
//
//
//
//});
//
