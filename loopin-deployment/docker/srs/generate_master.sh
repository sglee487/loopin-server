#!/bin/bash
set -euo pipefail

# 모드 파싱
MODE="create"
if [[ "${1:-}" == "--cleanup" ]]; then
  MODE="cleanup"
  shift
fi

# SRS exec 치환변수 인자: [vhost] [app] [stream]
VHOST="${1:-__defaultVhost__}"
APP="${2:-live}"
STREAM="${3:-mystream}"

# 원본 앱이 live가 아니면(예: abr/*) 아무 것도 안 함 → 자기복제 방지
if [[ "$APP" != "live" ]]; then
  exit 0
fi

# srs.conf의 hls_path와 동일해야 플레이어가 접근 가능
HLS_ROOT="/usr/local/srs/objs/nginx/html"

# 마스터를 원본 위치에 둠: /live/<stream>/master.m3u8
MASTER_DIR="$HLS_ROOT/$APP/$STREAM"
MASTER_FILE="$MASTER_DIR/master.m3u8"

if [[ "$MODE" == "cleanup" ]]; then
  rm -f "$MASTER_FILE" || true
  echo "🧹 cleaned: $MASTER_FILE"
  exit 0
fi

mkdir -p "$MASTER_DIR"

# ABR 출력은 /abr/<app>/<stream>_<variant>/index.m3u8 로 가정
cat > "$MASTER_FILE" <<EOF
#EXTM3U
#EXT-X-VERSION:3
#EXT-X-INDEPENDENT-SEGMENTS

# 1080p (60fps)
#EXT-X-STREAM-INF:BANDWIDTH=5500000,RESOLUTION=1920x1080,FRAME-RATE=60
/abr/${APP}/${STREAM}_1080p/index.m3u8

# 720p (60fps)
#EXT-X-STREAM-INF:BANDWIDTH=3200000,RESOLUTION=1280x720,FRAME-RATE=60
/abr/${APP}/${STREAM}_720p/index.m3u8

# 480p (30fps)
#EXT-X-STREAM-INF:BANDWIDTH=1500000,RESOLUTION=854x480,FRAME-RATE=30
/abr/${APP}/${STREAM}_480p/index.m3u8

# 360p (30fps)
#EXT-X-STREAM-INF:BANDWIDTH=900000,RESOLUTION=640x360,FRAME-RATE=30
/abr/${APP}/${STREAM}_360p/index.m3u8
EOF

echo "✅ Master playlist created: $MASTER_FILE"
