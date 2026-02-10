# Build

```
ollama create duke -f duke.Modelfile
```

# Test prompts

## Ollama client

Write OllamaApiClient Java class to call following Ollama API endpoints:

Generate a chat message
```
curl http://localhost:11434/api/chat -d '{
  "model": "gemma3",
  "messages": [
    {
      "role": "user",
      "content": "why is the sky blue?"
    }
  ]
}'
```
Response:
```
{
  "model": "<string>",
  "created_at": "2023-11-07T05:31:56Z",
  "message": {
    "role": "assistant",
    "content": "<string>",
    "thinking": "<string>",
    "tool_calls": [
      {
        "function": {
          "name": "<string>",
          "description": "<string>",
          "arguments": {}
        }
      }
    ],
    "images": [
      "<string>"
    ]
  },
  "done": true,
  "done_reason": "<string>",
  "total_duration": 123,
  "load_duration": 123,
  "prompt_eval_count": 123,
  "prompt_eval_duration": 123,
  "eval_count": 123,
  "eval_duration": 123,
  "logprobs": [
    {
      "token": "<string>",
      "logprob": 123,
      "bytes": [
        123
      ],
      "top_logprobs": [
        {
          "token": "<string>",
          "logprob": 123,
          "bytes": [
            123
          ]
        }
      ]
    }
  ]
}
```

List models
```
curl http://localhost:11434/api/tags
```
Response:
```
{
  "models": [
    {
      "name": "gemma3",
      "modified_at": "2025-10-03T23:34:03.409490317-07:00",
      "size": 3338801804,
      "digest": "a2af6cc3eb7fa8be8504abaf9b04e88f17a119ec3f04a3addf55f92841195f5a",
      "details": {
        "format": "gguf",
        "family": "gemma",
        "families": [
          "gemma"
        ],
        "parameter_size": "4.3B",
        "quantization_level": "Q4_K_M"
      }
    }
  ]
}
```
