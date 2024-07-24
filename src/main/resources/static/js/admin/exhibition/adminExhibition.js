// 유틸리티 함수
const utils = {
  parseDateRange(dateString) {
    if (!dateString) return { startDate: null, endDate: null };

    const splitStr = dateString.includes(" - ") ? " - " : "~";
    const [start, end] = dateString.trim().split(splitStr);

    function formatDate(date) {
      if (!date) return null;
      if (date.includes("-") && date.length > 10) return date.substring(0, 10);
      if (date.length === 8)
        return date.replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
      if (date.includes("-") && date.length === 10) return date;
      return date.slice(0, 10);
    }

    return {
      startDate: formatDate(start?.trim()),
      endDate: formatDate(end?.trim()),
    };
  },

  createPageButton(text, onClick) {
    const button = document.createElement("button");
    button.innerText = text;
    button.addEventListener("click", onClick);
    return button;
  },
};

// 전역 변수
let exhibition = [];
let exhibitionDB = [];
let currentPage = 1;
const itemsPerPage = 10;
const maxPageButtons = 10;

// 데이터 관리 함수
async function manageExhibitions(action, data) {
  try {
    let response;
    switch (action) {
      case "save":
        response = await axios.post(
          "/admin/exhibition-regulate/exhibition",
          data
        );
        console.log("save : ", data);
        alert(`${data.length} 데이터가 저장되었습니다.`);
        break;
      case "delete":
        response = await axios.delete(
          "/admin/exhibition-regulate/db-exhibition",
          { data: data.map((v) => v.id) }
        );
        alert("삭제완료");
        break;
      case "update":
        response = await axios.patch(
          "/admin/exhibition-regulate/db-exhibition",
          data
        );
        alert("수정 완료");
        break;
    }
    await fetchData();
  } catch (error) {
    console.error(`Error ${action}ing exhibition:`, error);
    alert(`${action} 중 오류가 발생했습니다.`);
  }
}

// 렌더링 함수
function renderData(allData, targetId, isPagination = false) {
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const pageData = allData.slice(startIndex, endIndex);

  if (!isPagination) {
    const tableBody = document.getElementById(targetId);
    tableBody.innerHTML = "";

    // API 리스트인 경우에만 정렬 적용
    const dataToRender =
      targetId === "list2" ? sortDataByDBExistence(allData) : allData;

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = dataToRender.slice(startIndex, endIndex);

    pageData.forEach((item, index) => {
      const { startDate, endDate } = utils.parseDateRange(item.PERIOD);
      item.startDate =
        (item.startDate || startDate || "").substring(0, 10) || "미정";
      item.endDate = (item.endDate || endDate || "").substring(0, 10) || "미정";

      item.start_date = item.startDate;
      item.end_date = item.endDate;

      const row = createTableRow(
        item,
        startIndex + index,
        targetId === "list2"
      );
      tableBody.innerHTML += row;
    });

    if (targetId === "list1") {
      addEditButtonListeners();
    }
  } else {
    const totalPages = Math.ceil(allData.length / itemsPerPage);
    const paginationElement = document.getElementById(targetId);
    paginationElement.innerHTML = "";

    let startPage = Math.max(currentPage - Math.floor(maxPageButtons / 2), 1);
    let endPage = Math.min(startPage + maxPageButtons - 1, totalPages);

    appendPageButton(paginationElement, "처음", () =>
      goToPage(1, allData, targetId)
    );
    if (currentPage > 1) {
      appendPageButton(paginationElement, "이전", () =>
        goToPage(currentPage - 1, allData, targetId)
      );
    }

    for (let i = startPage; i <= endPage; i++) {
      appendPageButton(
        paginationElement,
        i,
        () => goToPage(i, allData, targetId),
        i === currentPage
      );
    }

    if (currentPage < totalPages) {
      appendPageButton(paginationElement, "다음", () =>
        goToPage(currentPage + 1, allData, targetId)
      );
    }
    appendPageButton(paginationElement, "마지막", () =>
      goToPage(totalPages, allData, targetId)
    );
  }
}

function createTableRow(item, index, isApiList) {
  if (isApiList) {
    const existsInDB = exhibitionDB.some(
      (dbItem) => dbItem.localId === item.LOCAL_ID
    );
    return `
      <tr>
        <td scope="col" class="td-index">${
          existsInDB
            ? "DB 존재"
            : `<input type="checkbox" class="row-checkbox" data-index="${index}">`
        }</td>
        <td scope="col" class="td-number">${index + 1}</td>
        <td scope="col" title="${item.TITLE}">${item.TITLE}</td>
        <td scope="col" title="${item.CNTC_INSTT_NM}">${item.CNTC_INSTT_NM}</td>
        <td scope="col" title="${item.startDate}">${item.startDate}</td>
        <td scope="col" title="${item.endDate}">${item.endDate}</td>
      </tr>
    `;
  } else {
    return `
      <tr>
        <td scope="col" class="td-index"><input type="checkbox" class="row-checkbox" data-index="${index}"></td>
        <td scope="col" class="td-number">${index + 1}</td>
        <td scope="col" class="td-title" title="${item.title}">${
      item.title
    }</td>
        <td scope="col" class="td-artist" title="${item.artist}">${
      item.artist
    }</td>
        <td scope="col" title="${item.museum}">${item.museum}</td>
        <td scope="col" title="${item.startDate}">${item.startDate}</td>
        <td scope="col" title="${item.endDate}">${item.endDate}</td>
        <td scope="col" class="td-price" title="${item.price}">${
      item.price
    }</td>
        <td scope="col"><img class="resize-img" src="${
          item.image
        }" style="width: 150px;"></img></td>
        <td scope="col"><button class="edit-button">수정</button></td>
      </tr>
    `;
  }
}

function appendPageButton(container, text, onClick, isActive = false) {
  const button = utils.createPageButton(text, onClick);
  if (isActive) button.classList.add("active");
  container.appendChild(button);
}

function goToPage(page, allData, targetId) {
  currentPage = page;
  renderData(allData, targetId.replace("pageNum", "list"));
  renderData(allData, targetId, true);
}

// 검색 함수
async function search(type) {
  const title = $(`#searchTitle${type === "DB" ? "DB" : ""}`).val();
  const artist = type === "DB" ? $("#searchArtistDB").val() : null;
  const museum = $(`#searchMuseum${type === "DB" ? "DB" : ""}`).val();

  let url =
    type === "DB"
      ? "/exhibition/search-exhibition"
      : "/admin/exhibition-regulate/exhibition";
  let params = new URLSearchParams();

  if (title) params.append("title", title);
  if (artist) params.append("artist", artist);
  if (museum) params.append("museum", museum);

  try {
    const response = await axios.get(`${url}?${params.toString()}`);
    let data = response.data;

    if (type !== "DB") {
      data = data.filter(
        (v) =>
          (!title || v.TITLE.includes(title)) &&
          (!museum || v.CNTC_INSTT_NM.includes(museum))
      );
    }

    // 검색 결과 저장 및 currentPage 초기화
    if (type === "DB") {
      exhibitionDB = data;
    } else {
      exhibition = data;
    }
    currentPage = 1;

    const targetList = `list${type === "DB" ? "1" : "2"}`;
    const targetPagination = `pageNum${type === "DB" ? "1" : "2"}`;

    renderData(data, targetList);
    renderData(data, targetPagination, true);
  } catch (error) {
    console.error("검색 중 오류 발생:", error);
  }
}

// 데이터 가져오기
async function fetchData() {
  try {
    const [response1, response2] = await Promise.all([
      axios.get("/admin/exhibition-regulate/exhibition"),
      axios.get("/admin/exhibition-regulate/db-exhibition"),
    ]);

    exhibition = response1.data;
    exhibitionDB = response2.data;

    renderData(exhibition, "list2");
    renderData(exhibition, "pageNum2", true);
    renderData(exhibitionDB, "list1");
    renderData(exhibitionDB, "pageNum1", true);
  } catch (error) {
    console.error("데이터를 가져오는 중 오류 발생:", error);
  }
}

// 이벤트 리스너 등록 함수
function registerEventListeners() {
  document.getElementById("apiInsertBtn").addEventListener("click", () => {
    const checkedData = getCheckedData("list2");
    manageExhibitions("save", checkedData);
  });

  document.getElementById("dbDeleteBtn").addEventListener("click", () => {
    const checkedData = getCheckedData("list1");
    manageExhibitions("delete", checkedData);
  });

  document.getElementById("dbUpdateBtn").addEventListener("click", () => {
    const checkedData = getCheckedData("list1");
    manageExhibitions("update", checkedData);
  });

  document
    .getElementById("searchButtonDB")
    .addEventListener("click", () => search("DB"));
  document
    .getElementById("searchButton")
    .addEventListener("click", () => search("API"));
}

function getCheckedData(listId) {
  const checkedBoxes = document.querySelectorAll(
    `#${listId} .row-checkbox:checked`
  );
  return Array.from(checkedBoxes).map((checkbox) => {
    const index = parseInt(checkbox.getAttribute("data-index"));
    return listId === "list2" ? exhibition[index] : exhibitionDB[index];
  });
}

function addEditButtonListeners() {
  const editButtons = document.querySelectorAll("#list1 .edit-button");
  editButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const row = this.closest("tr");
      makeRowEditable(row);
    });
  });
}

function sortDataByDBExistence(data) {
  return data.sort((a, b) => {
    const aExistsInDB = exhibitionDB.some(
      (item) => item.localId === a.LOCAL_ID
    );
    const bExistsInDB = exhibitionDB.some(
      (item) => item.localId === b.LOCAL_ID
    );
    if (aExistsInDB && !bExistsInDB) return 1;
    if (!aExistsInDB && bExistsInDB) return -1;
    return 0;
  });
}

function makeRowEditable(row) {
  const cells = row.querySelectorAll("td:not(:first-child):not(:last-child)");
  cells.forEach((cell, index) => {
    if (index !== 0) {
      // 번호 열은 수정 불가
      const originalContent = cell.textContent;
      cell.innerHTML = `<input type="text" value="${originalContent}">`;
    }
  });

  const actionsCell = row.querySelector("td:last-child");
  actionsCell.innerHTML = `
    <button onclick="saveRowChanges(this)">저장</button>
    <button onclick="cancelRowEdit(this)">취소</button>
  `;
}

function saveRowChanges(button) {
  const row = button.closest("tr");
  const inputs = row.querySelectorAll('input[type="text"]');
  const updatedData = {};
  const fields = ["title", "artist", "museum", "startDate", "endDate", "price"];

  inputs.forEach((input, index) => {
    updatedData[fields[index]] = input.value;
  });

  const rowIndex = parseInt(
    row.querySelector(".row-checkbox").getAttribute("data-index")
  );
  exhibitionDB[rowIndex] = { ...exhibitionDB[rowIndex], ...updatedData };

  renderData(exhibitionDB, "list1");
}

function cancelRowEdit(button) {
  renderData(exhibitionDB, "list1");
}

// 초기화 함수
async function init() {
  await fetchData();
  registerEventListeners();
}

// 문서 로드 완료 시 초기화
$(document).ready(init);
