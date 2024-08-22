$(document).ready(function() {
      loadAllCulturalProperties();

      function loadAllCulturalProperties() {
          $.ajax({
              url: '/cultural-properties/getAll',
              type: 'GET',
              success: function(data) {
                  console.log('데이터 수신 성공:', data);
                  renderCulturalProperties(data);
                  getUserId().then(userId => {
                      loadUserInterest(userId);
                  }).catch(error => {
                      console.error('사용자 ID 요청 중 오류 발생:', error);
                  });
              },
              error: function(xhr, status, error) {
                  console.error('AJAX 요청 실패:', status, error);
              }
          });
      }


    function renderCulturalProperties(properties) {
        const gallerySection = $('#gallery-section');
        gallerySection.empty();

        if (!properties || properties.length === 0) {
            gallerySection.append('<p class="empty-message">등록된 문화재가 없습니다.</p>');
            return;
        }

        properties.forEach(function(property) {
            const item = $('<div>').addClass('all-cultural-property-item');
            const link = $('<a>').attr('href', `/cultural-properties/detail/${property.id}`);
            const card = $('<div>').addClass('all-card').attr('data-cultural-property-id', property.id);
            const img = $('<img>')
                .attr('src', property.mainImgUrl && property.mainImgUrl.trim() !== ''
                    ? property.mainImgUrl
                    : 'http://www.cha.go.kr/unisearch/images/no_image.gif'
                )
                .attr('alt', property.culturalPropertiesName)
                .on('error', function() {
                    $(this).attr('src', 'http://www.cha.go.kr/unisearch/images/no_image.gif');
                });
            const info = $('<div>').addClass('all-cultural-property-info');
            const title = $('<div>').addClass('all-title').text(property.culturalPropertiesName);
            const location = $('<div>').addClass('all-location').text((property.region && property.district) ? `${property.region} ${property.district}` : '위치정보가 없습니다').css('margin-bottom', '0');
            const category = $('<div>').addClass('all-category').text(property.categoryName);
            const dynasty = $('<div>').addClass('all-dynasty').text(property.dynasty);

            // 별점 추가
            const ratingWrapper = $('<div>').addClass('rating-wrapper');
            const star = $('<span>').addClass('star').text('★ ');
            const averageRating = $('<span>').addClass('average-rating').text(Number(property.averageRating).toFixed(1));
            ratingWrapper.append(star, averageRating);

            // 찜과 관심없음 버튼 추가
            const badgeDiv = $('<div>').addClass('badge-container');
            const likeButton = $('<button>').addClass('interest-button like-button');
            const dislikeButton = $('<button>').addClass('interest-button dislike-button');
            const likeImg = $('<img>').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png').attr('alt', '찜');
            const dislikeImg = $('<img>').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png').attr('alt', '관심없음');

            likeButton.append(likeImg);
            dislikeButton.append(dislikeImg);
            badgeDiv.append(likeButton, dislikeButton);

            // 버튼 이벤트 핸들러
            likeButton.click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                handleInterestClick(property.id, 'LIKE', $(this), dislikeButton);
            });

            dislikeButton.click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                handleInterestClick(property.id, 'DISLIKE', $(this), likeButton);
            });

            card.append(img, badgeDiv);
            info.append(title, category, location, dynasty, ratingWrapper);
            link.append(card, info);
            item.append(link);
            gallerySection.append(item);
        });
    }

    function handleInterestClick(culturalPropertiesId, interestType, clickedButton, otherButton) {
        const isActive = clickedButton.hasClass('active');

        if (interestType === 'LIKE') {
            if (isActive) {
                // LIKE가 이미 활성화되어 있다면 상태를 제거
                removeInterest(culturalPropertiesId, interestType, clickedButton);
            } else {
                // LIKE 추가
                addLike(culturalPropertiesId);
            }
        } else if (interestType === 'DISLIKE') {
            if (isActive) {
                // DISLIKE가 이미 활성화되어 있다면 상태를 제거
                removeInterest(culturalPropertiesId, interestType, clickedButton);
            } else {
                // DISLIKE 추가
                addDislike(culturalPropertiesId);
            }
        }

        // 서로의 상태를 제거하지 않도록 추가된 부분
        if (interestType === 'LIKE' && otherButton.hasClass('active')) {
            otherButton.removeClass('active');
            otherButton.find('img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png'); // 원래 이미지로 복원
        } else if (interestType === 'DISLIKE' && otherButton.hasClass('active')) {
            otherButton.removeClass('active');
            otherButton.find('img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png'); // 원래 이미지로 복원
        }
    }

    function addLike(culturalPropertiesId) {
        $.ajax({
            url: '/cultural-properties/addLike',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log('찜 추가 성공:', response);
                alert('찜이 추가되었습니다.'); // 찜 추가 알림

                // UI 업데이트
                const card = $(`.all-card[data-cultural-property-id="${culturalPropertiesId}"]`);
                card.find('.like-button').addClass('active');  // LIKE 버튼 활성화
                card.find('.dislike-button').removeClass('active');  // DISLIKE 버튼 비활성화
                card.find('.like-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png'); // 이미지 변경
                card.find('.dislike-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png'); // 이미지 변경
            },
            error: function(xhr, status, error) {
                console.error('찜 추가 실패:', status, error);
                alert('찜 추가 실패: ' + error);
            }
        });
    }

    function addDislike(culturalPropertiesId) {
        $.ajax({
            url: '/cultural-properties/addDislike',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log('관심없음 추가 성공:', response);
                alert('관심없음이 추가되었습니다.'); // 관심없음 추가 알림

                // UI 업데이트
                const card = $(`.all-card[data-cultural-property-id="${culturalPropertiesId}"]`);
                card.find('.dislike-button').addClass('active');  // DISLIKE 버튼 활성화
                card.find('.like-button').removeClass('active');  // LIKE 버튼 비활성화
                card.find('.like-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png'); // 이미지 변경
                card.find('.dislike-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downRed.png'); // 이미지 변경
            },
            error: function(xhr, status, error) {
                console.error('관심없음 추가 실패:', status, error);
                alert('관심없음 추가 실패: ' + error);
            }
        });
    }

    function removeInterest(culturalPropertiesId, interestType, clickedButton) {
        $.ajax({
            url: '/cultural-properties/removeInterest',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log(`${interestType} 제거 성공:`, response);
                alert(`${interestType === 'LIKE' ? '찜이 제거되었습니다.' : '관심없음이 제거되었습니다.'}`); // 제거 알림

                clickedButton.removeClass('active');  // 버튼 상태 비활성화

                // UI 업데이트: 이미지 원래 상태로 복원
                const card = $(`.all-card[data-cultural-property-id="${culturalPropertiesId}"]`);
                if (interestType === 'LIKE') {
                    card.find('.like-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png'); // 원래 이미지로 복원
                } else if (interestType === 'DISLIKE') {
                    card.find('.dislike-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png'); // 원래 이미지로 복원
                }
            },
            error: function(xhr, status, error) {
                console.error(`${interestType} 제거 실패:`, status, error);
                alert(`${interestType} 제거 실패: ` + error);
            }
        });
    }

    function loadUserInterest(userId) {
        $.ajax({
            url: '/cultural-properties/getInterest',
            type: 'GET',
            data: { userId: userId },
            success: function(interests) {
                interests.forEach(function(interest) {
                    const card = $(`.all-card[data-cultural-property-id="${interest.culturalPropertiesId}"]`);
                    if (interest.interestType === 'LIKE') {
                        card.find('.like-button').addClass('active');
                        card.find('.like-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png');
                    } else if (interest.interestType === 'DISLIKE') {
                        card.find('.dislike-button').addClass('active');
                        card.find('.dislike-button img').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downRed.png');
                    }
                });
            },
            error: function(xhr, status, error) {
                console.error('Failed to load user interests:', error);
            }
        });
    }

    // 페이지 로드 시 관심 상태 로드
    loadUserInterest();



    function getUserId() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/cultural-properties/getUserId',
                type: 'GET',
                success: function(response) {
                  const userId = response.userId;
                  console.log('User ID:', userId);
                  resolve(userId);
                },
                error: function(xhr, status, error) {
                  console.error('사용자 ID 로드 실패:', status, error);
                  reject(error);
                }
            });
        });
    }



    // 페이지가 로드될 때 사용자 ID 요청 및 찜 상태 로드
    $(document).ready(function() {
        getUserId().then(userId => {
            loadUserInterest(userId); // 사용자 ID를 이용해 찜 상태 로드
        }).catch(error => {
            console.error('사용자 ID 요청 중 오류 발생:', error);
        });
    });




    // 검색 버튼 클릭 이벤트 핸들러
    $('#searchButton').click(function(e) {
        e.preventDefault(); // 기본 이벤트 제거

        var formData = $('#searchForm').serialize(); // 폼 데이터 직렬화

        $.ajax({
            type: 'GET',
            url: '/cultural-properties/searchMain', // 컨트롤러 매핑 경로
            data: formData,
            success: function(response) {
                // 검색 결과를 처리하는 로직을 여기에 작성
                console.log(response); // 검색 결과 콘솔에 로그

                // response가 배열인 경우
                const properties = response; // 응답을 직접 배열로 사용
                renderCulturalProperties(properties); // 검색 결과를 전달하여 함수 호출


                // 찜 정보를 사용자 ID로 불러오기
                getUserId().then(userId => {
                    loadUserInterest(userId); // 찜 상태 로드
                }).catch(error => {
                    console.error('사용자 ID 요청 중 오류 발생:', error);
                });
            },
            error: function(xhr, status, error) {
                console.error('Ajax 오류 발생: ', error);

            }
        });
    });

    // 초기화 버튼 클릭 이벤트 핸들러
    $('#resetButton').click(function(e) {
        e.preventDefault();
        $('#searchForm')[0].reset();
        loadAllCulturalProperties();
    });

//// 추천 탭이 기본으로 선택되어 있다면 추천 문화재 로드
//        if ($('.genre-item[data-target="recommend"]').hasClass('active')) {
//            loadRecommendedCulturalProperties();
//        }<div class="genre-item" data-target="all">전체 문화재 목록</div>

    const genreItems = document.querySelectorAll('.genre-item');
    const tabPanes = document.querySelectorAll('.tab-pane');

    genreItems.forEach(item => {
        item.addEventListener('click', function() {
            const target = this.getAttribute('data-target');

            genreItems.forEach(item => item.classList.remove('active'));
            this.classList.add('active');

            tabPanes.forEach(pane => pane.classList.remove('show', 'active'));
            document.getElementById(target).classList.add('show', 'active');

            if (target === 'all') {
               loadAllCulturalProperties();
            } else if (target === 'news') {
               loadNewsArticles();
            } else if (target === 'video') {
               loadVideos();
            }
        });
    });



    // 뉴스 로드 함수
    function loadNewsArticles() {
        $.ajax({
            url: '/cultural-properties/news',
            method: 'GET',
            success: function(data) {
              console.log('뉴스 기사를 성공적으로 수신:', data);
              renderNewsArticles(data);
            },
            error: function(xhr, status, error) {
              console.error('AJAX 요청 실패:', status, error);
            }
        });
    }

    // 뉴스 렌더링 함수
    function renderNewsArticles(articles) {
        const newsList = $('#news-list');
        newsList.empty();

        if (!Array.isArray(articles) || articles.length === 0) {
            newsList.append('<li class="list-group-item">등록된 문화재 관련 뉴스가 없습니다.</li>');
            return;
        }

        articles.forEach(function(article) {
            const listItem = $('<li>').addClass('list-group-item');
            if (article.imgUrl) {
              const image = $('<img>').attr('src', article.imgUrl).addClass('img-fluid mb-2');
              listItem.append(image);
            }
            const title = $('<h5>').addClass('mb-1').text(article.title);
            const content = $('<p>').addClass('mb-1').text(article.content);
            const date = $('<small>').addClass('text-muted').text(article.date);
            //              const link = $('<a>').attr('href', article.link).addClass('btn btn-primary btn-sm mt-2').text('자세히 보기').css('margin-left', '2rem');

//            const link = $('<a>').attr('href', article.link).addClass('btn btn-outline-success').text('자세히 보기').css('margin-left', '2rem');

            const link = $('<a>').attr('href', article.link).addClass('btn btn-success').text('자세히 보기').css('margin-left', '2rem');


            listItem.append(title, content, date, link);
            newsList.append(listItem);
        });
    }

    // 비디오 로드 함수
    function loadVideos() {
        const query = "국가유산";
        $.ajax({
            url: '/cultural-properties/videos?query=' + encodeURIComponent(query),
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            success: function(videos) {
              console.log('받은 비디오 데이터:', videos);
              renderVideos(videos);
            },
            error: function(xhr, status, error) {
              console.error('비디오 로드 실패:', status, error);
            }
        });
    }

    // 비디오 렌더링 함수
    function renderVideos(videos) {
        const videoList = $('#video-list');
        videoList.empty();

        if (!Array.isArray(videos) || videos.length === 0) {
            videoList.append('<br><p>등록된 영상이 없습니다.</p>');
            return;
        }

        videos.forEach(function(video) {
            const colDiv = $('<div>').addClass('col-md-4 mb-4');
//            const cardDiv = $('<div>').addClass('card');
            const cardDiv = $('<div>').addClass('card video-card');
            const cardBody = $('<div>').addClass('card-body');
            const title = $('<h5>').addClass('card-title').html(video.title);
            const videoId = video.link.split('v=')[1];
            const iframe = $('<iframe>')
                .attr('src', 'https://www.youtube.com/embed/' + videoId)
                .attr('class', 'embed-responsive-item')
                .attr('allowfullscreen', true)
                .css('width', '100%')
                .css('height', '250px');

            cardBody.append(title, iframe);
            cardDiv.append(cardBody);
            colDiv.append(cardDiv);
            videoList.append(colDiv);
        });
    }



    function loadRecommendedCulturalProperties() {
        $.ajax({
            url: '/cultural-properties/main/recommend',
            type: 'GET',
            success: function(data) {
                renderRecommendedCulturalProperties(data);

                // 찜 정보를 사용자 ID로 불러오기
                getUserId().then(userId => {
                    loadUserInterest(userId); // 찜 상태 로드
                }).catch(error => {
                    console.error('사용자 ID 요청 중 오류 발생:', error);
                });

            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패:', status, error);
            }
        });
    }

    function renderRecommendedCulturalProperties(properties) {
        const gallerySection = $('#recommended-gallery-section');
        gallerySection.empty();

        if (!properties || properties.length === 0) {
            gallerySection.append('<p class="empty-message">추천할 문화재가 없습니다.</p>');
            return;
        }

        properties.forEach(function(property) {
            const item = $('<div>').addClass('all-cultural-property-item');
            const link = $('<a>').attr('href', `/cultural-properties/detail/${property.id}`);
            const card = $('<div>').addClass('all-card').attr('data-cultural-property-id', property.id);
//            const img = $('<img>').attr('src', property.mainImgUrl || '/img/festival/noPhoto.png').attr('alt', property.culturalPropertiesName);
            const img = $('<img>')
                .attr('src', property.mainImgUrl && property.mainImgUrl.trim() !== ''
                    ? property.mainImgUrl
                    : 'http://www.cha.go.kr/unisearch/images/no_image.gif'
                )
                .attr('alt', property.culturalPropertiesName)
                .on('error', function() {
                    $(this).attr('src', 'http://www.cha.go.kr/unisearch/images/no_image.gif');
                });
            const info = $('<div>').addClass('all-cultural-property-info');
            const title = $('<div>').addClass('all-title').text(property.culturalPropertiesName);
            const location = $('<div>').addClass('all-location').text((property.region && property.district) ? `${property.region} ${property.district}` : '위치정보가 없습니다');
            const category = $('<div>').addClass('all-category').text(property.categoryName);
            const dynasty = $('<div>').addClass('all-dynasty').text(property.dynasty);

            // 별점 추가
            const ratingWrapper = $('<div>').addClass('rating-wrapper');
            const star = $('<span>').addClass('star').text('★ ');
            const averageRating = $('<span>').addClass('average-rating').text(Number(property.averageRating).toFixed(1));
            ratingWrapper.append(star, averageRating);

            // 찜과 관심없음 버튼 추가
            const badgeDiv = $('<div>').addClass('badge-container');
            const likeButton = $('<button>').addClass('interest-button like-button');
            const dislikeButton = $('<button>').addClass('interest-button dislike-button');
            const likeImg = $('<img>').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png').attr('alt', '찜');
            const dislikeImg = $('<img>').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png').attr('alt', '관심없음');

            likeButton.append(likeImg);
            dislikeButton.append(dislikeImg);
            badgeDiv.append(likeButton, dislikeButton);


            // 버튼 이벤트 핸들러
            likeButton.click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                handleInterestClick(property.id, 'LIKE', $(this), dislikeButton);
            });

            dislikeButton.click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                handleInterestClick(property.id, 'DISLIKE', $(this), likeButton);
            });

            card.append(img, badgeDiv);
            info.append(title, category, location, dynasty, ratingWrapper);
            link.append(card, info);
            item.append(link);
            gallerySection.append(item);
        });
    }



    // 기존의 genre-item 클릭 이벤트 리스너에 추가
    $('.genre-item[data-target="recommend"]').on('click', function() {
        loadRecommendedCulturalProperties();
    });

    // 페이지 로드 시 추천 문화재 로드
    $(document).ready(function() {

        // 추천 탭이 기본으로 선택되어 있다면 추천 문화재 로드
        if ($('.genre-item[data-target="recommend"]').hasClass('active')) {
            loadRecommendedCulturalProperties();
        }
    });


    //전체 목록 페이지 연결
    var urlParam = new URLSearchParams(window.location.search);
    var paramValue = urlParam.get('param');

    if(paramValue=="fromDetail"){

        $('#allList').click();
        paramValue = "";

    }




});