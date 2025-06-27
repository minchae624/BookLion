document.addEventListener("DOMContentLoaded", () => {
  fetch("/api/questions")
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById("question-list");
      tbody.innerHTML = "";
      data.forEach((q, idx) => {
        const row = document.createElement("tr");
        row.style.textAlign = "center";
        const status = q.status === "SOLVED" ? "해결" : "미해결";
        const statusColor = q.status === "SOLVED" ? "green" : "red";
        row.innerHTML = `
          <td>${idx + 1}</td>
          <td>${q.category}</td>
          <td><a href="qna_detail.html?id=${q.id}">${q.title}</a></td>
          <td>${q.user?.username || "익명"}</td>
          <td>${q.writingtime?.substring(0, 10) || "-"}</td>
          <td style="color: ${statusColor};">${status}</td>
          <td>${q.viewCount}</td>
        `;
        tbody.appendChild(row);
      });
    })
    .catch(err => console.error("질문 목록 실패:", err));
});
