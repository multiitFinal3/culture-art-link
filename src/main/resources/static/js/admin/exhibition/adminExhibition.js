// ~ 기준으로 날짜 자르기
function parseDateRange(dateString) {
  if (!dateString) {
    return {
      startDate: null,
      endDate: null,
    };
  }
  const [start, end] = dateString?.split("~");

  function formatDate(date) {
    if (date.includes("-") && date.length === 10) {
      return date;
    }

    return date.replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
  }

  return {
    startDate: formatDate(start),
    endDate: formatDate(end),
  };
}

// init 함수
$(document).ready(function () {
  // 페이징
  const itemsPerPage = 10;
  let currentPage = 1;
  const maxPageButtons = 10;
  let exhibition = [];
  let exhibitionDB = [];

  async function searchButton() {
    const title = $("#searchTitle").val();
    const artist = $("#searchArtist").val();
    const museum = $("#searchMuseum").val();

    let url = "/exhibition/search-exhibition?";

    if (title) {
      url += `title=${encodeURIComponent(title)}&`;
    }
    if (artist) {
      url += `artist=${encodeURIComponent(artist)}&`;
    }
    if (museum) {
      url += `museum=${encodeURIComponent(museum)}&`;
    }
    const response1 = await axios.get(url);
    exhibitionDB = response1.data;

    console.log("search data : ", response1.data);
    renderTable(response1.data, "list1");
    renderPagination(response1.data, "pageNum1");
  }

  // 데이터 가져와서 table에 세팅
  async function fetchData() {
    try {
      const response1 = await axios.get(
        "/admin/exhibition-regulate/exhibition"
      );

      const response2 = await axios.get(
        "/admin/exhibition-regulate/db-exhibition"
      );
      exhibition = response1.data;
      renderTable(response1.data, "list2");
      renderPagination(response1.data, "pageNum2");

      console.log("response2 : ", response2);
      exhibitionDB = response2.data;
      renderTable(response2.data, "list1");
      renderPagination(response2.data, "pageNum1");
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
    }
  }

  // 테이블 모양 만들기
  function renderTable(allData, tableId) {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = allData.slice(startIndex, endIndex);

    const tableBody = document.getElementById(tableId);
    tableBody.innerHTML = "";

    pageData.forEach((item, index) => {
      const { startDate, endDate } = parseDateRange(item?.PERIOD);
      item.startDate = item.startDate ? item.startDate : startDate;
      item.endDate = item.endDate ? item.endDate : endDate;
      item.start_date = item.endDate;
      item.end_date = item.endDate;

      let row = "";

      if (tableId === "list2") {
        row = `
                <tr>
                  <td scope="col"><input type="checkbox" class="row-checkbox" data-index="${
                    startIndex + index
                  }"></td>
                  <td scope="col">${startIndex + index + 1}</td>
                  <td scope="col">${item.TITLE}</td>
                  <td scope="col">${item.CNTC_INSTT_NM}</td>
                  <td scope="col">${item.startDate}</td>
                  <td scope="col">${item.endDate}</td>
                </tr>
              `;
      } else {
        row = `
                <tr>
                  <td scope="col"><input type="checkbox" class="row-checkbox" data-index="${
                    startIndex + index
                  }"></td>
                  <td scope="col">${startIndex + index + 1}</td>
                  <td scope="col">${item.title}</td>
                  <td scope="col">${item.artist}</td>
                  <td scope="col">${item.museum}</td>
                  <td scope="col">${item.startDate}</td>
                  <td scope="col">${item.endDate}</td>
                  <td scope="col">${item.price}</td>
                  <td scope="col"><img class="resize-img" src="${
                    item.image
                  }"></img></td>
                   <td scope="col"><button class="edit-button">수정</button></td>
                </tr>
              `;
      }
      tableBody.innerHTML += row;
    });

    if (tableId === "list1") {
      const editButtons = document.querySelectorAll("#list1 .edit-button");
      // 수정 버튼 기능
      editButtons.forEach((button) => {
        button.addEventListener("click", function () {
          const row = this.closest("tr");
          makeRowEditable(row);
        });
      });
    }
  }

  // 페이징 버튼 클릭에 따른 데이터 렌더링
  function renderPagination(allData, pageNumId) {
    const totalPages = Math.ceil(allData.length / itemsPerPage);
    const paginationElement = document.getElementById(pageNumId);
    paginationElement.innerHTML = "";

    let startPage = Math.max(currentPage - Math.floor(maxPageButtons / 2), 1);
    let endPage = startPage + maxPageButtons - 1;

    if (endPage > totalPages) {
      endPage = totalPages;
      startPage = Math.max(endPage - maxPageButtons + 1, 1);
    }

    // 이전 버튼
    if (startPage > 1) {
      const prevButton = createPageButton("이전", () => {
        currentPage = startPage - 1;
        renderTable(allData, pageNumId === "pageNum2" ? "list2" : "list1");
        renderPagination(allData, pageNumId);
      });
      paginationElement.appendChild(prevButton);
    }

    // 페이지 번호 버튼
    for (let i = startPage; i <= endPage; i++) {
      const button = createPageButton(i, () => {
        currentPage = i;
        renderTable(allData, pageNumId === "pageNum2" ? "list2" : "list1");
        renderPagination(allData, pageNumId);
      });
      if (i === currentPage) {
        button.classList.add("active");
      }
      paginationElement.appendChild(button);
    }

    // 다음 버튼
    if (endPage < totalPages) {
      const nextButton = createPageButton("다음", () => {
        currentPage = endPage + 1;
        renderTable(allData, pageNumId === "pageNum2" ? "list2" : "list1");
        renderPagination(allData, pageNumId);
      });
      paginationElement.appendChild(nextButton);
    }
  }

  // 추가 버튼 기능
  function extractCheckedData() {
    const checkedBoxes = document.querySelectorAll(
      "#list2 .row-checkbox:checked"
    );
    // 체크된 데이터가 exhibition에서 몇 번째 데이터인지 가져옴
    const checkedData = Array.from(checkedBoxes).map((checkbox) => {
      const index = parseInt(checkbox.getAttribute("data-index"));
      return exhibition[index];
    });

    // 세이브
    saveExhibsions(checkedData);
    // 예시: 알림으로 체크된 데이터 수 표시
  }

  async function saveExhibsions(list) {
    if (list.length === 0) {
      alert("Please select");
    }
    const response = await axios.post(
      "/admin/exhibition-regulate/exhibition",
      list,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    alert(`${list.length} 데이터가 저장되었습니다.`);
    fetchData();
  }

  // 페이지 버튼 생성 함수
  function createPageButton(text, onClick) {
    const button = document.createElement("button");
    button.innerText = text;
    button.addEventListener("click", onClick);
    return button;
  }

  // 데이터 삭제 기능
  async function deleteExhibisionDB() {
    const checkedBoxes = document.querySelectorAll(
      "#list1 .row-checkbox:checked"
    );
    const checkedData = Array.from(checkedBoxes).map((checkbox) => {
      const index = parseInt(checkbox.getAttribute("data-index"));
      return {
        ...exhibitionDB[index],
        subDescription: exhibition[index].sub_description,
      };
    });

    if (checkedData.length === 0) {
      alert("Please select");
    }
    const response = await axios.delete(
      "/admin/exhibition-regulate/db-exhibition",
      { data: checkedData.map((v) => v.id) }
    );

    alert("삭제완료");
    fetchData();
  }

  // 데이터 수정 기능
  async function updateExhibisionDB() {
    const checkedBoxes = document.querySelectorAll(
      "#list1 .row-checkbox:checked"
    );
    const checkedData = Array.from(checkedBoxes).map((checkbox) => {
      const index = parseInt(checkbox.getAttribute("data-index"));
      return exhibitionDB[index];
    });

    if (checkedData.length === 0) {
      alert("Please select items to update");
      return;
    }

    try {
      const response = await axios.patch(
        "/admin/exhibition-regulate/db-exhibition",
        checkedData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      alert("수정 완료");
      fetchData();
    } catch (error) {
      console.error("Error updating exhibition:", error);
      alert("수정 중 오류가 발생했습니다.");
    }
  }

  // 수정 버튼 클릭 시 수정할 수 있게 해줌
  function makeRowEditable(row) {
    const cells = row.querySelectorAll("td:not(:first-child):not(:last-child)");
    cells.forEach((cell) => {
      if (cell.cellIndex !== 1) {
        // 번호 열은 수정 불가
        const originalContent = cell.textContent;
        cell.innerHTML = `<input type="text" value="${originalContent}">`;
      }
    });

    const saveButton = document.createElement("button");
    saveButton.textContent = "저장";
    saveButton.onclick = () => saveRowChanges(row);

    const cancelButton = document.createElement("button");
    cancelButton.textContent = "취소";
    cancelButton.onclick = () => cancelRowEdit(row);

    const actionsCell = row.querySelector("td:last-child");
    actionsCell.appendChild(saveButton);
    actionsCell.appendChild(cancelButton);
  }

  function saveRowChanges(row) {
    const inputs = row.querySelectorAll('input[type="text"]');
    const updatedData = {};

    inputs.forEach((input, index) => {
      const fieldName = [
        "title",
        "artist",
        "museum",
        "startDate",
        "endDate",
        "price",
      ][index];
      updatedData[fieldName] = input.value;
    });

    const rowIndex = parseInt(
      row.querySelector(".row-checkbox").getAttribute("data-index")
    );
    exhibitionDB[rowIndex] = {
      ...exhibitionDB[rowIndex],
      ...updatedData,
    };

    renderTable(exhibitionDB, "list1");
    // 여기에 서버로 업데이트된 데이터를 보내는 API 호출을 추가할 수 있습니다.
  }

  function cancelRowEdit(row) {
    renderTable(exhibitionDB, "list1");
  }

  // 체크된 데이터 추출 버튼에 이벤트 리스너 추가
  document
    .getElementById("apiInsertBtn")
    .addEventListener("click", extractCheckedData);

  document
    .getElementById("dbDeleteBtn")
    .addEventListener("click", deleteExhibisionDB);

  document
    .getElementById("dbUpdateBtn")
    .addEventListener("click", updateExhibisionDB);

  document
    .getElementById("searchButton")
    .addEventListener("click", searchButton);

  // 초기 데이터 로드
  fetchData();
});
