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

//      function renderCulturalProperties(properties) {
//          const gallerySection = $('#gallery-section');
//          gallerySection.empty();
//
//          if (properties.length === 0) {
//              gallerySection.append('<p>등록된 문화재가 없습니다.</p>');
//              return;
//          }
//
//          const row = $('<div>').addClass('row');
//          properties.forEach(function(property) {
//              const col = $('<div>').addClass('col-md-4 mb-4');
//              const card = $('<div>').addClass('card h-100').attr('data-cultural-property-id', property.id);
//              const img = $('<img>').addClass('card-img-top').attr('src', property.mainImgUrl).attr('alt', property.culturalPropertiesName);
//              const cardBody = $('<div>').addClass('card-body');
//              const title = $('<h5>').addClass('card-title').text(property.culturalPropertiesName);
//              const category = $('<p>').addClass('card-text').text(property.categoryName);
//              const location = $('<p>').addClass('card-text').text(`${property.region} ${property.district}`);
//
//              cardBody.append(title, category, location);
//              card.append(img, cardBody);
//              col.append(card);
//              row.append(col);
//          });
//
//          gallerySection.append(row);
//          addInterestFunctionality();
//      }

function renderCulturalProperties(properties) {
    const gallerySection = $('#gallery-section');
    gallerySection.empty();

    if (properties.length === 0) {
        gallerySection.append('<p>등록된 문화재가 없습니다.</p>');
        return;
    }

    properties.forEach(function(property) {
        const item = $('<div>').addClass('all-cultural-property-item');
        const link = $('<a>').attr('href', `/cultural-properties/detail/${property.id}`);
        const card = $('<div>').addClass('all-card');
        const img = $('<img>').attr('src', property.mainImgUrl).attr('alt', property.culturalPropertiesName);
        const info = $('<div>').addClass('all-cultural-property-info');
        const title = $('<div>').addClass('all-title').text(property.culturalPropertiesName);
        const location = $('<div>').addClass('all-location').text(`${property.region} ${property.district}`);
        const category = $('<div>').addClass('all-category').text(property.categoryName);

        card.append(img);
        info.append(title, location, category);
        link.append(card, info);
        item.append(link);
        gallerySection.append(item);
    });

    addInterestFunctionality();
}

      function addInterestFunctionality() {
          $('.card').each(function() {
              const card = $(this);
              const culturalPropertiesId = card.data('cultural-property-id');

              const badgeDiv = $('<div>').addClass('badge-container');
              const likeBadge = $('<span>').addClass('badge bg-primary me-1').text('♥').css('cursor', 'pointer');
              const starBadge = $('<span>').addClass('badge bg-secondary').text('☆').css('cursor', 'pointer');

              badgeDiv.append(likeBadge, starBadge);
              card.append(badgeDiv);

              likeBadge.click(function(e) {
                  e.preventDefault();
                  e.stopPropagation();
                  handleInterestClick(culturalPropertiesId, 'LIKE', card, likeBadge, starBadge);
              });

              starBadge.click(function(e) {
                  e.preventDefault();
                  e.stopPropagation();
                  handleInterestClick(culturalPropertiesId, 'DISLIKE', card, likeBadge, starBadge);
              });
          });
      }

      function handleInterestClick(culturalPropertiesId, interestType, card, activeBadge, inactiveBadge) {
          const isActive = activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked');

          if (isActive) {
              removeInterest(culturalPropertiesId, interestType);
              activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked', false);
              card.css('background-color', 'white');
          } else {
              addInterest(culturalPropertiesId, interestType);
              activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked', true);
              inactiveBadge.data(interestType === 'LIKE' ? 'disliked' : 'liked', false);
              card.css('background-color', interestType === 'LIKE' ? '#ffe5ea' : '#dfdfdf');
          }
      }

      function addInterest(culturalPropertiesId, interestType) {
          $.ajax({
              url: '/cultural-properties/addInterest',
              type: 'POST',
              data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
              success: function(response) {
                  console.log(interestType + ' 추가 성공:', response);
                  alert(response);
              },
              error: function(xhr, status, error) {
                  console.error(interestType + ' 추가 실패:', status, error);
                  alert(interestType + ' 추가 실패: ' + error);
              }
          });
      }

      function removeInterest(culturalPropertiesId, interestType) {
          $.ajax({
              url: '/cultural-properties/removeInterest',
              type: 'POST',
              data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
              success: function(response) {
                  console.log(interestType + ' 제거 성공:', response);
                  alert(interestType + '가 취소되었습니다');
              },
              error: function(xhr, status, error) {
                  console.error(interestType + ' 제거 실패:', status, error);
                  alert(interestType + ' 제거 실패: ' + error);
              }
          });
      }

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

      function loadUserInterest(userId) {
          $.ajax({
              url: '/cultural-properties/getInterest',
              type: 'GET',
              data: { userId: userId },
              success: function(data) {
                  data.forEach(function(item) {
                      const card = $(`.card[data-cultural-property-id="${item.culturalPropertiesId}"]`);
                      if (card.length) {
                          if (item.interestType === 'LIKE') {
                              card.css('background-color', '#ffe5ea');
                              card.find('.badge.bg-primary').data('liked', true);
                          } else if (item.interestType === 'DISLIKE') {
                              card.css('background-color', '#dfdfdf');
                              card.find('.badge.bg-secondary').data('disliked', true);
                          }
                      }
                  });
              },
              error: function(xhr, status, error) {
                  console.error('찜 정보 로드 실패:', status, error);
              }
          });
      }

//      // 검색 버튼 클릭 이벤트 핸들러
//      $('#searchButton').click(function(e) {
//          e.preventDefault();
//          var formData = $('#searchForm').serialize();
//          $.ajax({
//              type: 'GET',
//              url: '/cultural-properties/searchMain',
//              data: formData,
//              success: function(response) {
//                  console.log(response);
//                  renderCulturalProperties(response);
//                  getUserId().then(userId => {
//                      loadUserInterest(userId);
//                  }).catch(error => {
//                      console.error('사용자 ID 요청 중 오류 발생:', error);
//                  });
//              },
//              error: function(xhr, status, error) {
//                  console.error('Ajax 오류 발생: ', error);
//              }
//          });
//      });

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

                // 결과를 화면에 표시하는 코드
                renderCulturalProperties(response.content); // 검색 결과를 전달하여 함수 호출

//                createPagination(response.totalPages, 1, new URLSearchParams(formData)); // 검색된 페이지 수와 초기 페이지 설정

                // 찜 정보를 사용자 ID로 불러오기
                getUserId().then(userId => {
                    loadUserInterest(userId); // 찜 상태 로드
                }).catch(error => {
                    console.error('사용자 ID 요청 중 오류 발생:', error);
                });
            },
            error: function(xhr, status, error) {
                console.error('Ajax 오류 발생: ', error);
                // 오류 처리 로직을 추가하세요
            }
        });
    });

      // 초기화 버튼 클릭 이벤트 핸들러
      $('#resetButton').click(function(e) {
          e.preventDefault();
          $('#searchForm')[0].reset();
          loadAllCulturalProperties();
      });

//      // 카테고리 필터 클릭 이벤트
//      $('.category-item').click(function() {
//          const category = $(this).text().trim();
//          $.ajax({
//              type: 'GET',
//              url: '/cultural-properties/getByCategory',
//              data: { category: category },
//              success: function(response) {
//                  renderCulturalProperties(response);
//              },
//              error: function(xhr, status, error) {
//                  console.error('카테고리 필터링 오류:', error);
//              }
//          });
//      });


      const genreItems = document.querySelectorAll('.genre-item');
      const tabPanes = document.querySelectorAll('.tab-pane');

      genreItems.forEach(item => {
          item.addEventListener('click', function() {
              const target = this.getAttribute('data-target');

              genreItems.forEach(item => item.classList.remove('active'));
              this.classList.add('active');

              tabPanes.forEach(pane => pane.classList.remove('show', 'active'));
              document.getElementById(target).classList.add('show', 'active');

              if (target === 'news') {
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
              const link = $('<a>').attr('href', article.link).addClass('btn btn-primary btn-sm mt-2').text('자세히 보기').css('margin-left', '2rem');

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
              const cardDiv = $('<div>').addClass('card');
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

//      // 페이지 로드 시 뉴스와 비디오 로드
//      loadNewsArticles();
//      loadVideos();
  });